package pro.sky.animalshelterapp.service.serviceImpl;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;
import pro.sky.animalshelterapp.repository.ChatUserRepository;
import pro.sky.animalshelterapp.service.AnimalService;
import pro.sky.animalshelterapp.service.ChatUserService;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.animalshelterapp.model.Animal.AnimalTypes.NO_ANIMAL;
import static pro.sky.animalshelterapp.model.ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL;
import static pro.sky.animalshelterapp.model.ChatUser.ChatUserStatus.GUEST;
import static pro.sky.animalshelterapp.service.MessageConstants.CONTACT_ME_TEXT;
import static pro.sky.animalshelterapp.service.MessageConstants.SUCCESS_SAVING_TEXT;


@Service
public class ChatUserServiceImpl implements ChatUserService {

    private final Logger logger = LoggerFactory.getLogger(ChatUserServiceImpl.class);

    private static final String REGEX_BOT_MESSAGE = "([\\W+]+)(\\s)(\\+7\\d{3}[-.]?\\d{3}[-.]?\\d{4})(\\s)([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)";

    private final ChatUserRepository repository;
    private final AnimalService animalService;

    public ChatUserServiceImpl(ChatUserRepository repository, AnimalService animalService) {
        this.repository = repository;
        this.animalService = animalService;
    }

    /**
     * Сохраняет созданного пользователя в репозитории
     *
     * @param chatUser - Созданный польхователь
     * @return savedUser - Данные польователя сохранены в репозиторий
     */

    @Override
    public ChatUser save(ChatUser chatUser) {
        chatUser.setStatus(GUEST);
        chatUser.setAnimal(animalService.getAnimalByName(NO_ANIMAL));
        return repository.save(chatUser);
    }

    @Override
    public ChatUser edit(Long id, Long chatId, ChatUser chatUser, ChatUser.ChatUserStatus status, Animal.AnimalTypes type) {
        chatUser.setId(id);
        chatUser.setChatId(chatId);
        chatUser.setStatus(status);
        chatUser.setAnimal(animalService.getAnimalByName(type));
        return repository.save(chatUser);
    }


    /**
     * Создаем нового пользователя без бота через Swagger/Postman
     */

    @Override
    public ChatUser createChatUserByVolunteer(ChatUser chatUser, Animal.AnimalTypes type) {
        chatUser.setAnimal(animalService.getAnimalByName(type));
        logger.info("Was invoked method to create a chatUser by volunteer");
        return repository.save(chatUser);
    }

    /**
     * Parsing пользователя на имя, адрес почты и телефон
     *
     * ChatUserDataMessage получаем сообщение от пользователя
     */

    @Override
    public Optional<ChatUser> parse(String chatUserDataMessage, Long chatId) {
        logger.info("Parsing method has been called");
        Pattern pattern = Pattern.compile(REGEX_BOT_MESSAGE);
        Matcher matcher = pattern.matcher(chatUserDataMessage);
        ChatUser result = null;
        try {
            if (matcher.find()) {
                String name = matcher.group(1);
                String phoneNumber = matcher.group(3);
                String email = matcher.group(5);
                result = new ChatUser(name, phoneNumber, email);
                result.setChatId(chatId);
            }
        } catch (Exception e) {
            logger.error("Failed to parse ChatUser's data: " + chatUserDataMessage, e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public String registrationChatUser(Message inputMessage) {
        String outputMessage;
        Optional<ChatUser> parseResult = parse(inputMessage.text(), inputMessage.chat().id());
        if (parseResult.isPresent()) {
            long chatId = inputMessage.chat().id();
            if (getChatUserByChatId(chatId) == null) {
                logger.info("Parse result is valid");
                save(parseResult.get());
                outputMessage = SUCCESS_SAVING_TEXT;
            } else {
                logger.info("Data is already exists, it will be restored");
                ChatUser currentChatUser = getChatUserByChatId(chatId);
                Animal.AnimalTypes type = currentChatUser.getAnimal().getType();
                ChatUser editedChatUser = edit(currentChatUser.getId(), chatId, parseResult.get(), currentChatUser.getStatus(), type);
                logger.info("Client with id {} has been edited successfully: ", editedChatUser.getId());
                outputMessage = "Ваши данные успешно перезаписаны!";
            }
        } else {
            logger.info("Invalid registration data");
            outputMessage = CONTACT_ME_TEXT;
        }
        return outputMessage;
    }

    @Override
    public ChatUser getChatUserById(Long id) {
        logger.info("Was invoked method to find a chatUser by Id");
        return repository.findById(id).orElse(null);
    }

    @Override
    public ChatUser getChatUserByChatId(Long chatId) {
        logger.info("Was invoked method to find chatUser by chatId");
        return repository.findChatUserByChatId(chatId);
    }

    @Override
    public void deleteChatUserById(Long id) {
        logger.info("Was invoked method to delete a quest by Id");
        repository.deleteById(id);
    }

    @Override
    public Collection<ChatUser> getAllChatUsers() {
        logger.info("Was invoked method to get a list of all chatUsers");
        return repository.findAll();
    }

    @Override
    public List<ChatUser> getAllAdopters(ChatUser.ChatUserStatus status) {
        logger.info("Was invoked method to get a list of all chatUsers with definite status");
        return repository.findAllAdopters(status);
    }

    @Override
    public boolean adopterOnTrialExists(Long chatId) {
        return (repository.findChatUserByChatId(chatId) != null
                && repository.findChatUserByChatId(chatId).getStatus().equals(ADOPTER_ON_TRIAL));
    }

    @Override
    public List<ChatUser> getAdoptersWithEndOfTrial(ChatUser.ChatUserStatus status, LocalDate endTrialDate) {
        return repository.findAdoptersWithEndOfTrial(status, endTrialDate);
    }


    @Override
    public List<ChatUser> getAdoptersByReportStatusAndSentDate(Report.ReportStatus reportStatus, LocalDate sentDate) {
        return repository.findAdoptersByReportStatusAndSentDate(reportStatus, sentDate);
    }

    @Override
    public List<ChatUser> getAdoptersByStatusAndReportDate(ChatUser.ChatUserStatus status, LocalDate sentDate) {
        return repository.findAdoptersByStatusAndReportDate(status, sentDate);
    }

    @Override
    public List<ChatUser> getAdoptersByStatusAndExtendedTrial(ChatUser.ChatUserStatus status) {
        List<ChatUser> adoptersList = repository.findAllAdopters(ADOPTER_ON_TRIAL);
        List<ChatUser> adoptersWithExtendedTrial = new ArrayList<>();
        if (adoptersList != null) {
            for (ChatUser chatUser : adoptersList) {
                if (chatUser.getStartTrialDate().plusDays(30).equals(LocalDate.now())) {
                    adoptersWithExtendedTrial.add(chatUser);
                }
            }
        }
        return adoptersWithExtendedTrial;
    }
}
