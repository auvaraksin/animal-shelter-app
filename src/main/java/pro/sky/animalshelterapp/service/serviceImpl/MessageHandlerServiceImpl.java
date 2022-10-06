package pro.sky.animalshelterapp.service.serviceImpl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.listener.TelegramBotUpdatesListener;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;
import pro.sky.animalshelterapp.service.ChatUserService;
import pro.sky.animalshelterapp.service.MessageHandlerService;
import pro.sky.animalshelterapp.service.ReportService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import static pro.sky.animalshelterapp.service.MessageConstants.START_CMD;
import static pro.sky.animalshelterapp.service.constantsMessage.*;
import static pro.sky.animalshelterapp.service.constantsMessage.CONTACT_ME_TEXT;
import static pro.sky.animalshelterapp.service.constantsMessage.GREETINGS_TEXT;


@Service
public class MessageHandlerServiceImpl implements MessageHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot animalShelterBot;
    private final ChatUserService chatUserService;
    private final ReportService reportService;
    private static boolean registrationRequired = false;
    private static int sendingReportStatus = 0;
    private static String reportText;

    public MessageHandlerServiceImpl(TelegramBot animalShelterBot, ChatUserService chatUserService, ReportService reportService) {
        this.animalShelterBot = animalShelterBot;
        this.chatUserService = chatUserService;
        this.reportService = reportService;
    }

    @Override
    public void handleMessage(Message inputMessage, long chatId) {
        if (inputMessage.text() != null) {
            switch (inputMessage.text()) {

                // Общие команды

                case START_CMD:
                    sendMessage(chatId, GREETINGS_TEXT, chooseShelter());
                    break;
                case BACK_TO_CHOOSE_ANIMAL:
                    sendMessage(chatId, CHOOSE_OPTION, chooseShelter());
                    break;
                case CONTACT_ME_CMD:
                    if (chatUserService.getChatUserByChatId(chatId) != null && !chatUserService.getChatUserByChatId(chatId).getStatus().equals(ChatUser.ChatUserStatus.GUEST)) {
                        sendMessage(chatId, "Для изменения контактнаых данных, нажмите кнопку \"Позвать волонтера\". ");
                    } else {
                        sendMessage(chatId, CONTACT_ME_TEXT);
                        registrationRequired = true;
                    }
                    break;
                case CALL_VOLUNTEER_CMD:
                    String chatUserContact = inputMessage.chat().username();
                    if (chatUserContact == null) {
                        sendMessage(chatId, "Пожалуйста, отправьте ваш номер телефона формате +79991234567 без пробелов.");
                    } else {
                        sendMessage(chatId, CALL_VOLUNTEER_TEXT);
                        sendMessage(VOLUNTEERS_CHAT_ID, "Пожалуйста, свяжитесь с пользователем: https://t.me/" + chatUserContact);
                    }
                    break;
                case DOCUMENTS_CMD:
                    sendMessage(chatId, DOCUMENTS_TEXT);
                    break;
                case REFUSAL_REASONS_CMD:
                    sendMessage(chatId, REFUSAL_REASONS_TEXT);
                    break;
                case SEND_REPORT_CMD:
                    sendingReportStatus = 1;
                    if (chatUserService.adopterOnTrialExists(chatId)) {
                        if (!reportService.reportWasSentToday(LocalDate.now(), chatUserService.getChatUserByChatId(chatId).getId())) {
                            sendMessage(chatId, REPORT_FORM);
                        } else {
                            sendMessage(chatId, "Отчет был ранее отправлен.");
                        }
                    } else {
                        sendMessage(chatId, """
                                Вы не числитесь в базе данных усыновителей питомцев.\s

                                Позовите волонтёра для уточнения информации с помощью команды "Позвать волонтера".""");
                    }
                    break;
                case UNKNOWN_FILE:
                    sendMessage(chatId, UNKNOWN_FILE);
                    break;

                //Команды для собак

                case DOG_SHELTER_CMD:
                case BACK_TO_DOG_START_MENU_CMD:
                    sendMessage(chatId, "Добро пожаловать в приют для собак!\n" + CHOOSE_OPTION, dogStartMenuButtons());
                    break;
                case DOG_SHELTER_INFO_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, dogShelterInfoButtons());
                    break;
                case DOG_ABOUT_US_CMD:
                    sendMessage(chatId, DOG_ABOUT_US_TEXT);
                    break;
                case DOG_WORKING_HOURS_CMD:
                    sendMessage(chatId, DOG_WORKING_HOURS_TEXT);
                    break;
                case DOG_SECURITY_CONTACT_CMD:
                    sendMessage(chatId, DOG_SECURITY_CONTACT_TEXT);
                    break;
                case DOG_SAFETY_RECOMMENDATION_CMD:
                    sendMessage(chatId, DOG_SAFETY_RECOMMENDATION_TEXT);
                    break;
                case HOW_TO_TAKE_DOG_CMD:
                case BACK_TO_DOG_RECOMMENDATION_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, dogRecommendationButtons());
                    break;
                case MEET_THE_DOG_CMD:
                    sendMessage(chatId, MEET_THE_DOG_TEXT);
                    break;
                case DOG_TRANSPORTING_AND_ADVICE_CMD:
                case BACK_TO_DOG_TRANSPORT_AND_ADVICE_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, dogTransportAndAdviceDogButtons());
                    break;
                case DOG_TRANSPORTING_CMD:
                    sendMessage(chatId, DOG_TRANSPORTING_TEXT);
                    break;
                case DOG_ADVICE_CMD:
                    sendMessage(chatId, DOG_COMMON_ADVICE_TEXT, dogSpecificAdviceMenu());
                    break;
                case ADVICE_FOR_PUPPY_CMD:
                    sendMessage(chatId, ADVICE_FOR_PUPPY_TEXT);
                    break;
                case ADVICE_FOR_ADULT_DOG_CMD:
                    sendMessage(chatId, ADVICE_FOR_ADULT_DOG_TEXT);
                    break;
                case ADVICE_FOR_SPECIAL_DOG_CMD:
                    sendMessage(chatId, ADVICE_FOR_SPECIAL_DOG_TEXT);
                    break;
                case CYNOLOGIST_CMD:
                case BACK_TO_CYNOLOGIST_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, cynologistMenu());
                    break;
                case CYNOLOGIST_ADVICE_CMD:
                    sendMessage(chatId, CYNOLOGIST_ADVICE_TEXT);
                    break;
                case CYNOLOGIST_CONTACTS_CMD:
                    sendMessage(chatId, CYNOLOGIST_CONTACTS_TEXT);
                    break;

                //Команды для кошек

                case CAT_SHELTER_CMD:
                case BACK_TO_CAT_START_MENU_CMD:
                    sendMessage(chatId, "Добро пожаловать в приют для кошек!\n" + CHOOSE_OPTION, catStartMenuButtons());
                    break;
                case CAT_SHELTER_INFO_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, catShelterInfoButtons());
                    break;
                case CAT_ABOUT_US_CMD:
                    sendMessage(chatId, CAT_ABOUT_US_TEXT);
                    break;
                case CAT_WORKING_HOURS_CMD:
                    sendMessage(chatId, CAT_WORKING_HOURS_TEXT);
                    break;
                case CAT_SECURITY_CONTACT_CMD:
                    sendMessage(chatId, CAT_SECURITY_CONTACT_TEXT);
                    break;
                case CAT_SAFETY_RECOMMENDATION_CMD:
                    sendMessage(chatId, CAT_SAFETY_RECOMMENDATION_TEXT);
                    break;
                case HOW_TO_TAKE_CAT_CMD:
                case BACK_TO_CAT_RECOMMENDATION_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, catRecommendationButtons());
                    break;
                case MEET_THE_CAT_CMD:
                    sendMessage(chatId, MEET_THE_CAT_TEXT);
                    break;
                case CAT_TRANSPORTING_AND_ADVICE_CMD:
                case BACK_TO_CAT_TRANSPORT_AND_ADVICE_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, catTransportAndAdviceDogButtons());
                    break;
                case CAT_TRANSPORTING_CMD:
                    sendMessage(chatId, CAT_TRANSPORTING_TEXT);
                    break;
                case CAT_ADVICE_CMD:
                    sendMessage(chatId, CAT_COMMON_ADVICE_TEXT, catSpecificAdviceMenu());
                    break;
                case ADVICE_FOR_KITTEN_CMD:
                    sendMessage(chatId, ADVICE_FOR_KITTEN_TEXT);
                    break;
                case ADVICE_FOR_ADULT_CAT_CMD:
                    sendMessage(chatId, ADVICE_FOR_ADULT_CAT_TEXT);
                    break;
                case ADVICE_FOR_SPECIAL_CAT_CMD:
                    sendMessage(chatId, ADVICE_FOR_SPECIAL_CAT_TEXT);
                    break;
                case FELINOLOGIST_CMD:
                case BACK_TO_FELINOLOGIST_MENU_CMD:
                    sendMessage(chatId, CHOOSE_OPTION, felinologystMenu());
                    break;
                case FELINOLOGIST_ADVICE_CMD:
                    sendMessage(chatId, FELINOLOGIST_ADVICE_TEXT);
                    break;
                case FELINOLOGIST_CONTACTS_CMD:
                    sendMessage(chatId, FELINOLOGIST_CONTACTS_TEXT);
                    break;

                default:
                    if (inputMessage.text().startsWith("+") && inputMessage.text().length() == 12) {
                        sendMessage(VOLUNTEERS_CHAT_ID, "Свяжитесь с пользователем: https://t.me/" + inputMessage.text());
                        sendMessage(chatId, "Спасибо, волонтер свяжется с вами.");
                    } else if (registrationRequired) {
                        logger.info("Registration data has been sent");
                        sendMessage(chatId, chatUserService.registrationChatUser(inputMessage));
                        sendMessage(VOLUNTEERS_CHAT_ID, "Пользователь " + chatUserService.getChatUserByChatId(chatId).getName() +
                                " (" + chatUserService.getChatUserByChatId(chatId).getPhoneNumber() + ") оставил контакты для связи.");
                        registrationRequired = false;
                    } else if (sendingReportStatus == 1) {
                        sendMessage(chatId, PHOTO_REPORT_REQUIRED);
                        reportText = inputMessage.text();
                        sendingReportStatus = 2;
                    } else {
                        sendMessage(chatId, INVALID_NOTIFICATION_OR_CMD);
                    }
            }

        } else if (inputMessage.photo() != null && sendingReportStatus == 2) {
            File file = getFile(inputMessage);
            String filePath = animalShelterBot.getFullFilePath(file);
            Integer fileSize = Math.toIntExact(file.fileSize());

            try {

                Report report = reportService.handlePhoto(inputMessage, fileSize, filePath, reportText);
                sendMessage(chatId, "Спасибо! Ваш отчет отправлен волонтеру на проверку.");

                java.io.File localFile = reportService.downloadFile(filePath, inputMessage);

                ChatUser chatUser = chatUserService.getChatUserByChatId(chatId);

                sendMessage(VOLUNTEERS_CHAT_ID, "Вам поступил отчет на проверку: \n"
                        + "\n\uD83D\uDFE2Пользователь: "
                        + "\nId: " + chatUser.getId()
                        + "\nИмя: " + chatUser.getName()
                        + "\nИспытательный срок: " + chatUser.getStartTrialDate() + " - " + chatUser.getEndTrialDate()
                        + "\n\n\uD83D\uDFE2Отчет: "
                        + "\nId: " + report.getId()
                        + "\nНомер: " + reportService.countUserReports(chatUser.getId())
                        + "\nСодержание: " + report.getReportText());

                sendDocument(VOLUNTEERS_CHAT_ID, localFile);

            } catch (Exception e) {
                e.printStackTrace();
            }

            sendingReportStatus = 0;

        } else {
            sendMessage(chatId, UNKNOWN_FILE);
        }
    }

    private File getFile(Message inputMessage) {
        List<PhotoSize> photos = List.of(inputMessage.photo());
        PhotoSize photo = photos.stream()
                .max(Comparator.comparing(PhotoSize::fileSize)).orElse(null);
        GetFile request = new GetFile(photo.fileId());
        GetFileResponse getFileResponse = animalShelterBot.execute(request);
        return getFileResponse.file();
    }

    @Override
    public void sendMessage(Long chatId, String inputMessage, Keyboard keyboard) {
        SendMessage outputMessage = new SendMessage(chatId, inputMessage)
                .replyMarkup(keyboard);
        try {
            animalShelterBot.execute(outputMessage);
        } catch (Exception e) {
            logger.info("Exception was thrown in sendMessage method with keyboard ");
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Long chatId, String inputMessage) {
        SendMessage outputMessage = new SendMessage(chatId, inputMessage);
        try {
            animalShelterBot.execute(outputMessage);
        } catch (Exception e) {
            logger.info("Exception was thrown in sendMessage message method ");
            e.printStackTrace();
        }
    }

    @Override
    public void sendDocument(Long chatId, java.io.File file) {
        SendDocument photo = new SendDocument(chatId, file);
        try {
            animalShelterBot.execute(photo);
        } catch (Exception e) {
            logger.info("Exception was thrown in sendMessage message method ");
            e.printStackTrace();
        }
    }

    private static ReplyKeyboardMarkup chooseShelter() {
        logger.info("Choose shelter keyboard was called");
        return new ReplyKeyboardMarkup(
                CAT_SHELTER_CMD, DOG_SHELTER_CMD)
                .resizeKeyboard(true)
                .selective(true);
    }

    /**
     * Создание главного меню (кнопки) приюта для собак
     *
     * @return buttons
     */

    private static Keyboard dogStartMenuButtons() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(DOG_SHELTER_INFO_CMD),
                        new KeyboardButton(HOW_TO_TAKE_DOG_CMD),
                },
                new KeyboardButton[]{
                        new KeyboardButton(SEND_REPORT_CMD),
                        new KeyboardButton(CALL_VOLUNTEER_CMD)
                }, new KeyboardButton[]{
                new KeyboardButton(BACK_TO_CHOOSE_ANIMAL)
        })
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки в меню "о приюте"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup dogShelterInfoButtons() {
        logger.info("Dog shelter info keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{DOG_ABOUT_US_CMD, DOG_WORKING_HOURS_CMD},
                new String[]{DOG_SAFETY_RECOMMENDATION_CMD, DOG_SECURITY_CONTACT_CMD},
                new String[]{CONTACT_ME_CMD, CALL_VOLUNTEER_CMD},
                new String[]{BACK_TO_DOG_START_MENU_CMD})
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки "Как забрать собаку" из приют
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup dogRecommendationButtons() {
        logger.info("Dogs recommendations Keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{MEET_THE_DOG_CMD, DOCUMENTS_CMD},
                new String[]{DOG_TRANSPORTING_AND_ADVICE_CMD, CYNOLOGIST_CMD},
                new String[]{REFUSAL_REASONS_CMD, CONTACT_ME_CMD},
                new String[]{CALL_VOLUNTEER_CMD, BACK_TO_DOG_START_MENU_CMD})
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки в меню "документация по перевозке собаки и благоуйстроства дома для неё"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup dogTransportAndAdviceDogButtons() {
        logger.info("Dog's transportation and advice keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{DOG_TRANSPORTING_CMD, DOG_ADVICE_CMD},
                new String[]{BACK_TO_DOG_RECOMMENDATION_MENU_CMD}
        )
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопок в меню "Советы для разных типов собак: собак инвалидов, щенков и тд"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup dogSpecificAdviceMenu() {
        logger.info("Specific advice keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{ADVICE_FOR_PUPPY_CMD, ADVICE_FOR_ADULT_DOG_CMD, ADVICE_FOR_SPECIAL_DOG_CMD},
                new String[]{BACK_TO_DOG_TRANSPORT_AND_ADVICE_MENU_CMD}
        )
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопок в меню: "Контакты и рекомендации киноголов"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup cynologistMenu() {
        logger.info("Cynologists keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{CYNOLOGIST_ADVICE_CMD, CYNOLOGIST_CONTACTS_CMD},
                new String[]{BACK_TO_DOG_RECOMMENDATION_MENU_CMD}
        )
                .resizeKeyboard(true);
    }

    // Кнопки для кошек

    private static Keyboard catStartMenuButtons() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(CAT_SHELTER_INFO_CMD),
                        new KeyboardButton(HOW_TO_TAKE_CAT_CMD),
                },
                new KeyboardButton[]{
                        new KeyboardButton(SEND_REPORT_CMD),
                        new KeyboardButton(CALL_VOLUNTEER_CMD)
                }, new KeyboardButton[]{
                new KeyboardButton(BACK_TO_CHOOSE_ANIMAL)
        })
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки в меню "О приюте"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup catShelterInfoButtons() {
        logger.info("Cat shelter info keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{CAT_ABOUT_US_CMD, CAT_WORKING_HOURS_CMD},
                new String[]{CAT_SAFETY_RECOMMENDATION_CMD, CAT_SECURITY_CONTACT_CMD},
                new String[]{CONTACT_ME_CMD, CALL_VOLUNTEER_CMD},
                new String[]{BACK_TO_CAT_START_MENU_CMD})
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки в меню "Как забрать кошку/кота"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup catRecommendationButtons() {
        logger.info("Cats recommendations Keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{MEET_THE_CAT_CMD, DOCUMENTS_CMD},
                new String[]{CAT_TRANSPORTING_AND_ADVICE_CMD, FELINOLOGIST_CMD},
                new String[]{REFUSAL_REASONS_CMD, CONTACT_ME_CMD},
                new String[]{CALL_VOLUNTEER_CMD, BACK_TO_CAT_START_MENU_CMD})
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопки в меню "Рекомендации по перевозки и благоуйстройству кошки"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup catTransportAndAdviceDogButtons() {
        logger.info("Cat's transportation and advice keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{CAT_TRANSPORTING_CMD, CAT_ADVICE_CMD},
                new String[]{BACK_TO_CAT_RECOMMENDATION_MENU_CMD}
        )
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопок в меню для "Советы по благоустройству дома для кошек/ котят / котят - инвалидов"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup catSpecificAdviceMenu() {
        logger.info("Specific cat advice keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{ADVICE_FOR_KITTEN_CMD, ADVICE_FOR_ADULT_CAT_CMD, ADVICE_FOR_SPECIAL_CAT_CMD},
                new String[]{BACK_TO_CAT_TRANSPORT_AND_ADVICE_MENU_CMD}
        )
                .resizeKeyboard(true);
    }

    /**
     * Создание кнопок в меню "Контакты и рекомендации от Фелинологов"
     *
     * @return buttons
     */

    private static ReplyKeyboardMarkup felinologystMenu() {
        logger.info("Cynologist keyboard was called");
        return new ReplyKeyboardMarkup(
                new String[]{FELINOLOGIST_ADVICE_CMD, FELINOLOGIST_CONTACTS_CMD},
                new String[]{BACK_TO_CAT_RECOMMENDATION_MENU_CMD}
        )
                .resizeKeyboard(true);
    }


    @Scheduled(cron = "0 0 12 * * *")
    public void sendReminders() {
        sendReminderAboutLackOfReport();
        sendRemindersToVolunteerAboutEndOfTrial();
        sendNotificationAboutSuccessReport();
        sendNotificationAboutDeclinedReport();
        sendNotificationAboutResultOfTrial();
    }

    @Override
    public void sendReminderAboutLackOfReport() {
        List<ChatUser> adoptersList = chatUserService.getAllAdopters(ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL);
        if (!adoptersList.isEmpty()) {
            SendMessage reminderToUser = null;
            SendMessage reminderToVolunteer = null;
            for (ChatUser chatUser : adoptersList) {
                Report lastReport = reportService.getLastReportByUserId(chatUser.getId());
                if (lastReport != null && lastReport.getSentDate().isBefore(LocalDate.now())) {
                    reminderToUser = new SendMessage(chatUser.getChatId(), "Вчера мы не получили от Вас отчет о питомце. " +
                            "Пожалуйста, отправьте отчет. В противном случае волонтеры приюта будут обязаны лично проверять условия содержания животного. ");
                    if (lastReport.getSentDate().isBefore(LocalDate.now().minusDays(1))) {
                        reminderToVolunteer = new SendMessage(VOLUNTEERS_CHAT_ID, "Усыновитель " + chatUser.getName() + " " + chatUser.getPhoneNumber() + " не присылал отчет в течение двух дней. " +
                                "\nНеобходимо с ним связаться как можно скорее.");
                    }

                }
            }
            if (reminderToUser != null) {
                animalShelterBot.execute(reminderToUser);
            }
            if (reminderToUser != null) {
                animalShelterBot.execute(reminderToVolunteer);
            }
        }
    }

    @Override
    public void sendRemindersToVolunteerAboutEndOfTrial() {
        List<ChatUser> adoptersList = chatUserService.getAdoptersWithEndOfTrial(ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL, LocalDate.now());
        if (!adoptersList.isEmpty()) {
            SendMessage reminder = null;
            for (ChatUser chatUser : adoptersList) {
                reminder = new SendMessage(VOLUNTEERS_CHAT_ID, "Сегодня у усыновителя " + chatUser.getName() + " " + chatUser.getPhoneNumber() + " заканчивается испытательный срок. " +
                        "\nНеобходимо принять решение, прошел ли усыновитель испытательный срок или требуется продление срока.");
            }
            animalShelterBot.execute(reminder);
        }
    }

    @Override
    public void sendNotificationAboutSuccessReport() {
        List<ChatUser> adoptersListWithAcceptedReports = chatUserService.getAdoptersByReportStatusAndSentDate(Report.ReportStatus.ACCEPTED, LocalDate.now().minusDays(1));
        if (!adoptersListWithAcceptedReports.isEmpty()) {
            SendMessage reminder = null;
            for (ChatUser chatUser : adoptersListWithAcceptedReports) {
                reminder = new SendMessage(chatUser.getChatId(), "Поздравляем! Ваш вчерашний отчет был проверен и одобрен волонтером. Продолжайте в том же духе!");
            }
            animalShelterBot.execute(reminder);
        }
    }

    @Override
    public void sendNotificationAboutDeclinedReport() {
        List<ChatUser> adoptersListWithDeclinedReports = chatUserService.getAdoptersByReportStatusAndSentDate(Report.ReportStatus.DECLINED, LocalDate.now().minusDays(1));
        if (!adoptersListWithDeclinedReports.isEmpty()) {
            SendMessage reminder = null;
            for (ChatUser chatUser : adoptersListWithDeclinedReports) {
                reminder = new SendMessage(chatUser.getChatId(), BAD_REPORT_WARNING);
            }
            animalShelterBot.execute(reminder);
        }
    }

    @Override
    public void sendNotificationAboutResultOfTrial() {
        List<ChatUser> adopterListWithSuccessTrial = chatUserService.getAdoptersByStatusAndReportDate(ChatUser.ChatUserStatus.OWNER, LocalDate.now().minusDays(1));
        if (!adopterListWithSuccessTrial.isEmpty()) {
            SendMessage reminder;
            for (ChatUser chatUser : adopterListWithSuccessTrial) {
                reminder = new SendMessage(chatUser.getChatId(), TRIAL_PASSED);
                animalShelterBot.execute(reminder);
            }
        }

        List<ChatUser> adopterListWithTrialFailed = chatUserService.getAdoptersByStatusAndReportDate(ChatUser.ChatUserStatus.ADOPTER_TRIAL_FAILED, LocalDate.now());
        if (!adopterListWithTrialFailed.isEmpty()) {
            SendMessage reminder;
            for (ChatUser chatUser : adopterListWithTrialFailed) {
                reminder = new SendMessage(chatUser.getChatId(), TRIAL_NOT_PASSED);
                animalShelterBot.execute(reminder);
            }
        }

        List<ChatUser> adopterListWithExtendedTrial = chatUserService.getAdoptersByStatusAndExtendedTrial(ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL);
        if (!adopterListWithExtendedTrial.isEmpty()) {
            SendMessage reminder;
            for (ChatUser chatUser : adopterListWithExtendedTrial) {
                reminder = new SendMessage(chatUser.getChatId(), TRIAL_EXTENDED + chatUser.getEndTrialDate());
                animalShelterBot.execute(reminder);
            }
        }
    }
}