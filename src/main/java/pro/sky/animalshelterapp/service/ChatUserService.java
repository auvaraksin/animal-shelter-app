package pro.sky.animalshelterapp.service;

import com.pengrad.telegrambot.model.Message;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatUserService {

    ChatUser save(ChatUser chatUser);

    ChatUser edit(Long id, Long chatId, ChatUser chatUser, ChatUser.ChatUserStatus status, Animal.AnimalTypes type);

    ChatUser createChatUserByVolunteer(ChatUser chatUser, Animal.AnimalTypes type);

    Optional<ChatUser> parse(String chatUserDataMessage, Long chatId);

    String registrationChatUser(Message inputMessage);

    ChatUser getChatUserById(Long id);

    ChatUser getChatUserByChatId(Long chatId);

    void deleteChatUserById(Long id);

    Collection<ChatUser> getAllChatUsers();

    List<ChatUser> getAllAdopters(ChatUser.ChatUserStatus status);

    boolean adopterOnTrialExists(Long id);

    List<ChatUser> getAdoptersWithEndOfTrial(ChatUser.ChatUserStatus status, LocalDate endTrialDate);

    List<ChatUser> getAdoptersByReportStatusAndSentDate(Report.ReportStatus reportStatus, LocalDate sentDate);

    List<ChatUser>getAdoptersByStatusAndReportDate(ChatUser.ChatUserStatus status, LocalDate sentDate);

    List<ChatUser> getAdoptersByStatusAndExtendedTrial(ChatUser.ChatUserStatus status);

}