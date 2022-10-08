package pro.sky.animalshelterapp;

import com.pengrad.telegrambot.model.Message;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.model.Report;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static pro.sky.animalshelterapp.model.Animal.AnimalTypes.DOG;


public interface DataTest {

    String REGEX_BOT_MESSAGE = "([\\W+]+)(\\s)(\\+7\\d{3}[-.]?\\d{3}[-.]?\\d{4})(\\s)([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)";
    Animal.AnimalTypes TYPE = DOG;
    Long ANIMAL_ID = 1L;


    LocalDate SENT_DATE = LocalDate.now().minusDays(1);

    Long chatUSER_ID_1 = 1L;
    Long chatUSER_CHAT_ID_1 = 1L;
    String chatUSER_NAME_1 = "Гобозов Дмитрий";
    String chatUSER_PHONE_1 = "+79218890932";
    String chatUSER_EMAIL_1 = "dgoboz@gmail.com";
    ChatUser.ChatUserStatus chatUSER_STATUS_1 = ChatUser.ChatUserStatus.GUEST;

    Long chatUSER_ID_2 = 2L;
    Long chatUSER_CHAT_ID_2 = 2L;
    String chatUSER_NAME_2 = "Гобозова Людмила";
    String chatUSER_PHONE_2 = "+79056056696";
    String chatUSER_EMAIL_2 = "lgoboz@gmailcom";
    ChatUser.ChatUserStatus chatUSER_STATUS_2 = ChatUser.ChatUserStatus.GUEST;

    String chatUSER_MESSAGE_1 = "Гобозов Дмитрий +79218890932 dgoboz@gmail.com";
    Message REPORT_MESSAGE = new Message();

    ChatUser.ChatUserStatus chatUSER_STATUS_3 = ChatUser.ChatUserStatus.ADOPTER_ON_TRIAL;
    LocalDate TEST_TRIAL_DATE = LocalDate.of(2022,12,1);
    LocalDate START_TRIAL_DATE = LocalDate.now().minusDays(30);


    Long REPORT_ID_1 = 1L;
    String REPORT_TEXT_1 = "Все впорядке";
    String FILE_PATH_1 = "https://pocvetam.ru/wp-content/uploads/2019/08/kartinka-4-dekorativnyj-tabak-ochen-krasivo-cvetet.jpg";
    LocalDate SENT_DATE_1 = LocalDate.of(2022,1,1);
    Integer FILE_SIZE_1 = 1024;
    byte[] PREVIEW_1 = "f6d73k9r".getBytes(StandardCharsets.UTF_8);
    Report.ReportStatus REPORT_STATUS_1 = Report.ReportStatus.SENT;
    Long REPORT_ID_2 = 2L;
    String REPORT_TEXT_2 = "Все прекрасно";
    String FILE_PATH_2 = "https://api.telegram.org/photo2.jpg";
    LocalDate SENT_DATE_2 = LocalDate.of(2020,2,4);
    Report.ReportStatus REPORT_STATUS_2 = Report.ReportStatus.ACCEPTED;
    String FILE_PATH_3 = null;

}
