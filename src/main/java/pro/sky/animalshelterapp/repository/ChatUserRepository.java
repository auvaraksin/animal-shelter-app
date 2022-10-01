package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelterapp.model.ChatUser;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelterapp.model.Report;


//Репозиторий для ChatUser

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    @Query("select u from ChatUser u where u.id = ?1")
    ChatUser findUserById(Long id);

    @Query("select u from ChatUser u where u.chatId = ?1")
    ChatUser findChatUserByChatId(Long chatId);

    @Query("select u from ChatUser u where u.status = ?1")
    List<ChatUser> findAllAdopters(ChatUser.ChatUserStatus status);

    @Query("select u from ChatUser u where u.status = ?1 and u.endTrialDate = ?2")
    List<ChatUser> findAdoptersWithEndOfTrial(ChatUser.ChatUserStatus status, LocalDate endTrialDate);

    @Query("select u from ChatUser u inner join Report on u.id in (select r.clientId from Report as r where r.status = ?1 and r.sentDate = ?2)")
    List<ChatUser> findAdoptersByReportStatusAndSentDate(Report.ReportStatus reportStatus, LocalDate sentDate);

    @Query("select u from ChatUser u inner join Report on u.status = ?1 and u.id in (select r.clientId from Report as r where r.sentDate = ?2)")
    List<ChatUser> findAdoptersByStatusAndReportDate(ChatUser.ChatUserStatus status, LocalDate sentDate);

}