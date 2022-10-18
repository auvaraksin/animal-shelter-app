package pro.sky.animalshelterapp.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.exeptions.WrongDataSavingExeption;
import pro.sky.animalshelterapp.interfaces.ClientService;
import pro.sky.animalshelterapp.interfaces.MessageSourceService;
import pro.sky.animalshelterapp.interfaces.ReportService;
import pro.sky.animalshelterapp.models.Client;
import pro.sky.animalshelterapp.models.Report;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    /* variable 'state' provides exchange rules of text messages with telegram-bot */
    private String state = "default";
    private final MessageSourceService messageSourceService;

    private final ReportService reportService;
    private final ClientService clientService;

    public TelegramBotUpdatesListener(MessageSourceService messageSourceService, ClientService clientService, ReportService reportService) {
        this.messageSourceService = messageSourceService;
        this.clientService = clientService;
        this.reportService = reportService;
    }

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
   //Method sends messages about reports
    @Scheduled(cron = "@daily")
    public void run() {
        logger.info("run method is working");
         List<Client> clients = clientService.findAll().stream().toList();
         for (Client i:clients) {
            if (i.getDateOfStart()!=null&&i.getDateOfEnd()!=null) {
                //Если срок начался, а ни одного отчёта ещё нет
                if ((i.getDateOfStart().isBefore(LocalDate.now())) && reportService.findAllByClientName(i.getName()) == null) {
                    SendMessage message = new SendMessage(i.getChatId(), "Вы должны присылать отчёты о питомце!");
                    SendResponse response = telegramBot.execute(message);
                    if (!response.isOk()) {
                        response.errorCode();
                    }
                }
                //Если не прислан ежедневный отчёт
                if ((i.getDateOfStart().isBefore(LocalDate.now())
                        && i.getDateOfEnd().isAfter(LocalDate.now())
                        && getLastReport(i.getName()).getDate().isBefore(LocalDate.now()))) {
                    SendMessage message = new SendMessage(i.getChatId(), "Вы забыли прислать отчёт о питомце!");
                    SendResponse response = telegramBot.execute(message);
                    if (!response.isOk()) {
                        response.errorCode();
                    }
                }
                //Если всё в порядке и статус клиента true
                if (i.getDateOfEnd().equals(LocalDate.now()) && i.isStatus()) {
                    SendMessage message = new SendMessage(i.getChatId(), "Поздравляем! Вы прошли испытательный срок и становитесь" +
                            " полноправным хозяином питомца!");
                    SendResponse response = telegramBot.execute(message);
                    if (!response.isOk()) {
                        response.errorCode();
                    }
                }
                //Если срок уже прошёл, а статуса true нет
                if (i.getDateOfEnd().isBefore(LocalDate.now()) && !i.isStatus()) {
                    SendMessage message = new SendMessage(i.getChatId(), "Вы не прошли испытательный срок! Обратитесь к волонтёру!");
                    SendResponse response = telegramBot.execute(message);
                    if (!response.isOk()) {
                        response.errorCode();
                    }
                }
            }
         }
        logger.info("run method has worked");
    }
    //return the latest report and let to know the last date
     public Report getLastReport (String name) {
        List <Report> listOfReports = reportService.findAllByClientName(name).stream()
                .sorted(Comparator.comparingLong(Report::getId)).toList();
        return listOfReports.get(listOfReports.size()-1);
     }
    @Override
    public int process(List<Update> updates) {
        run();
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message() != null) {
                switch (state) {
                    case "default":
                        callMainMenu(update, state);
                        state = "main_menu";
                        logger.info("State: {}", state);
                        break;
                    case "main_menu":
                        callMainMenu(update, state);
                        logger.info("State: {}", state);
                        break;
                    case "general_information":
                        callNewUserConsultationMenu(update, state);
                        logger.info("State: {}", state);
                        break;
                    case "consultation_menu":
                        callConsultationMenuForAdoptivePerson(update, state);
                        logger.info("State: {}", state);
                        break;
                    case "consultation_menu_for_cat":
                        callConsultationMenuForAdoptivePersonForCats(update, state);
                        logger.info("State: {}", state);
                        break;
                    case "report_menu":
                        callReportMenu(update, state);
                        logger.info("State: {}", state);
                        break;
                    case "signup_mode":
                        callSignupMethod(update);
                        state = "main_menu";
                        logger.info("State: {}", state);
                        break;
                    case "report_mode":
                        if (checkReportQuality(update)) {
                            try {
                                callReportApplyMethod(update);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            state = "main_menu";
                            logger.info("State: {}", state);
                            break;
                        } else {
                            state = "report_mode_additional_check";
                            logger.info("State: {}", state);
                            break;
                        }
                    case "report_mode_additional_check":
                        try {
                            callReportApplyMethod(update);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        state = "main_menu";
                        logger.info("State: {}", state);
                        break;
                    default:
                        logger.info("State: {}", state);
                        break;
                }
            }
            if (update.callbackQuery() != null) {
                switch (update.callbackQuery().data()) {
                    case "/main_menu":
                        state = "main_menu";
                        callMainMenu(update);
                        logger.info("State: {}", state);
                        break;
                    case "/main_menu_01":
                        state = "general_information";
                        callNewUserConsultationMenu(update);
                        logger.info("State: {}", state);
                        break;
                    case "/main_menu_02":
                        state = "consultation_menu";
                        callConsultationMenuForAdoptivePerson(update);
                        logger.info("State: {}", state);
                        break;
                    case "/main_menu_02_01":
                        state = "consultation_menu_for_cat";
                        callConsultationMenuForAdoptivePersonForCats(update);
                        logger.info("State: {}", state);
                        break;
                    case "/main_menu_03":
                        state = "report_menu";
                        callReportMenu(update);
                        logger.info("State: {}", state);
                        break;

                    case "/menu_11":
                        showGeneralInformation(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_12":
                        showScheduleAddressAnotherUsefulInformation(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_13":
                        showGeneralSafetyRules(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_21":
                        showMeetingRulesWithPet(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_22":
                        showListOfRequiredDocumentsBeforeAdoptPet(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_23":
                        showRecommendationsAboutPetTransportationHandlingRules(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_24":
                        showRecommendationsAboutHouseholdConditionsForPuppy(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_25":
                        showRecommendationsAboutHouseholdConditionsForDog(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_26":
                        showRecommendationsAboutHouseholdConditionsForDisabledDog(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_27":
                        showRecommendationsOfDogBreedingSpecialist(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_28":
                        showListOfDogBreedingSpecialists(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_29":
                        showListOfRejectionCauses(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_121":
                        showMeetingRulesWithPetCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_122":
                        showListOfRequiredDocumentsBeforeAdoptPetCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_123":
                        showRecommendationsAboutPetTransportationHandlingRulesCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_124":
                        showRecommendationsAboutHouseholdConditionsForKitty(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_125":
                        showRecommendationsAboutHouseholdConditionsForCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_126":
                        showRecommendationsAboutHouseholdConditionsForDisabledCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_127":
                        showRecommendationsOfDogBreedingSpecialistCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_128":
                        showListOfDogBreedingSpecialistsCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_129":
                        showListOfRejectionCausesCat(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_31":
                        showReportPattern(update);
                        logger.info("State: {}", state);
                        break;
                    case "/menu_32":
                        state = "report_mode";
                        showGeneralMessageBeforeStartReportFilling(update);
                        logger.info("State: {}", state);
                        break;
                    case "/signup":
                        state = "signup_mode";
                        showGeneralSignupPatternMessage(update);
                        logger.info("State: {}", state);
                        break;
                    case "/call_volunteer":
                        callVolunteer(update);
                        logger.info("State: {}", state);
                        break;
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing general information about animal shelter */
    private void showGeneralInformation(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("info"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing schedule working time, address and another useful information about animal shelter */
    private void showScheduleAddressAnotherUsefulInformation(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("adress"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing general information about safety rules in the animal shelter */
    private void showGeneralSafetyRules(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("rules"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing meeting rules with pet */
    private void showMeetingRulesWithPet(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("animalMeetingRules"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of required documents before adopt a pet */
    private void showListOfRequiredDocumentsBeforeAdoptPet(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("documents"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about pet transportation handling rules */
    private void showRecommendationsAboutPetTransportationHandlingRules(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("transportingRules"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for puppy */
    private void showRecommendationsAboutHouseholdConditionsForPuppy(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("puppyHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for dog */
    private void showRecommendationsAboutHouseholdConditionsForDog(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("dogHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for disabled dog */
    private void showRecommendationsAboutHouseholdConditionsForDisabledDog(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("invalidDogHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing dog breeding specialist's pieces of advice */
    private void showRecommendationsOfDogBreedingSpecialist(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("kinolog"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of recommended dog breeding specialists */
    private void showListOfDogBreedingSpecialists(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("kinologList"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of causes of rejection decision */
    private void showListOfRejectionCauses(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("rejection"));
        SendResponse response = telegramBot.execute(message);
    }
    private void showMeetingRulesWithPetCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("catMeetingRules"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of required documents before adopt a pet */
    private void showListOfRequiredDocumentsBeforeAdoptPetCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("documentsForCat"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about pet transportation handling rules */
    private void showRecommendationsAboutPetTransportationHandlingRulesCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("TransportRulesCats"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for puppy */
    private void showRecommendationsAboutHouseholdConditionsForKitty(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("kittyHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for dog */
    private void showRecommendationsAboutHouseholdConditionsForCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("CatHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing recommendations about household conditions for disabled dog */
    private void showRecommendationsAboutHouseholdConditionsForDisabledCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("invalidCatHome"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing dog breeding specialist's pieces of advice */
    private void showRecommendationsOfDogBreedingSpecialistCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("veterinar"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of recommended dog breeding specialists */
    private void showListOfDogBreedingSpecialistsCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("veterinarList"));
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to read record in the Shelter table containing list of causes of rejection decision */
    private void showListOfRejectionCausesCat(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                messageSourceService.findById("catRejection"));
        SendResponse response = telegramBot.execute(message);
    }
    /* This method shows up requirements to the daily report form */
    private void showReportPattern(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                "<b>Ежедневный отчет должен содержать следующую информацию</b>\n" +
                        "<b><i>Фотографию животного</i></b>\n" +
                        "<b><i>Рацион животного</i></b>\n" +
                        "<b><i>Общее самочуствие и привыкание к новому месту</i></b>\n" +
                        "<b><i>Изменение поведения: отказ от старых привычек, приобретение новых привычек</i></b>");
        message.parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method shows up general message before start report filling procedure */
    private void showGeneralMessageBeforeStartReportFilling(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                "Заполните отчет по установленной форме, не забудьте приложить фотографию питомца");
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to create new record in the User table */
    private void callReportApplyMethod(Update update) throws IOException {
        if (update.message().photo() == null) {
            saveReportWithoutPhoto(update);
        }
        if (update.message().photo() != null && update.message().caption() == null) {
            saveReportWithoutText(update);
        }
        if (update.message().photo() != null && update.message().caption() != null) {
            saveReport(update);
        }
    }

    /* This method initializes fields in the report object except fields with data about photo */
    private void saveReportWithoutPhoto(Update update) {
        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setChatId(update.message().chat().id());
        report.setText(update.message().text());
        report.setStatus(false);
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Отчёт принят без фотографии. Статус отчета: не соответствует требованиям");
        SendResponse response = telegramBot.execute(message);
        reportService.createReport(report);
    }

    /* This method initializes fields in the report object except field with text */
    private void saveReportWithoutText(Update update) throws IOException {
        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setChatId(update.message().chat().id());
        report.setStatus(false);
        String fileId = update.message().photo()[3].fileId();
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        File file = getFileResponse.file();
        URL url = new URL("https://api.telegram.org/file/bot" + telegramBot.getToken() + "/" + file.filePath());
        InputStream inputStream = url.openStream();
        byte[] fileContent = inputStream.readAllBytes();
        report.setData(fileContent);
        report.setMediaType("image/" + getExtensions(file.filePath()));
        report.setFileSize(file.fileSize());
        inputStream.close();
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Отчёт принят без текста. Статус отчета: не соответствует требованиям");
        SendResponse response = telegramBot.execute(message);
        reportService.createReport(report);
    }

    /* This method initializes fields in the report object */
    public void saveReport(Update update) throws IOException{
        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setChatId(update.message().chat().id());
        report.setText(update.message().caption());
        report.setStatus(true);
        String fileId = update.message().photo()[3].fileId();
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        File file = getFileResponse.file();
        URL url = new URL("https://api.telegram.org/file/bot" + telegramBot.getToken() + "/" + file.filePath());
        InputStream inputStream = url.openStream();
        byte[] fileContent = inputStream.readAllBytes();
        report.setData(fileContent);
        report.setMediaType("image/" + getExtensions(file.filePath()));
        report.setFileSize(file.fileSize());
        inputStream.close();
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Отчёт принят. Статус отчета: соответствует требованиям");
        SendResponse response = telegramBot.execute(message);
        reportService.createReport(report);
    }

    private String getExtensions(String file) {
        return file.substring(file.lastIndexOf(".") + 1);
    }

    /* This method provides check a quality of reports */
    private Boolean checkReportQuality(Update update) {
        if (update.message().photo() == null) {
            SendMessage message = new SendMessage(update.message().chat().id(),
                    "В отчете отсутствует фотография. Дополните отчет фотографией питомца и повторите отправку");
            SendResponse response = telegramBot.execute(message);
            return false;
        }
        if (update.message().caption() == null) {
            SendMessage message = new SendMessage(update.message().chat().id(),
                    "В отчете отсутствует описание жизни питомца. Дополните отчет описанием жизни питомца и повторите отправку");
            SendResponse response = telegramBot.execute(message);
            return false;
        }
        return true;
    }

    /* This method shows up pattern of the contact data application form */
    private void showGeneralSignupPatternMessage(Update update) {
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                "<b>Введите свои контактные данные в формате</b>\n" +
                        "<b>КОНТАКТНЫЙ НОМЕР ТЕЛЕФОНА</b> (в формате):\n +(<i>код страны</i>)(<i>код города/оператора</i>)(<i>номер телефона</i>)\n" +
                        "<b>АДРЕС ЭЛЕКТРОННОЙ ПОЧТЫ</b> (в формате): emailaddress@mail.com\n" +
                        "<b>Например:</b>\n" +
                        "<i>Иванов Иван</i> " +
                        "<i>+71234567890</i> " +
                        "<i>ivan.ivanov@gmail.com</i>");
        message.parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates SQL-request to the database to create new record in the User table */
    private void callSignupMethod(Update update) {
        try {
            Client client = new Client(update.message().chat().id(),
                    update.message().chat().firstName(), parsingNameOfClient(update),
                    parsingContactsOfClient(update));
            if (!parsingNameOfClient(update).isEmpty() && !parsingContactsOfClient(update).isEmpty()) {
                clientService.createClient(client);
                SendMessage message = new SendMessage(update.message().chat().id(),
                        "Данные обработаны");
                SendResponse response = telegramBot.execute(message);
            } else {
                SendMessage message = new SendMessage(update.message().chat().id(),
                        "Данные не обработаны");
                SendResponse response = telegramBot.execute(message);
            }
        } catch (WrongDataSavingExeption e) {
            System.out.println("Ошибка сохранения данных");
        }
    }

    // This method parses client's name from the message content
    private String parsingNameOfClient(Update update) {
        String[] words = update.message().text().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String i : words) {
            if (i.charAt(0) == '+') {
                break;
            } else {
                sb.append(i);
                sb.append(" ");
            }
        }
        if (sb.isEmpty()) {
            return sb.toString();
        } else {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    // The method parses client contacts from string message
    private String parsingContactsOfClient(Update update) {
        if (update.message().text().contains(" +")) {
            return update.message().text().substring(update.message().text().indexOf("+"));
        } else {
            return "";
        }
    }

    /* This method generates and sends message to volunteer's chat */
    private void callVolunteer(Update update) {
        String userId = "";
        userId += update.callbackQuery().from().id();
        System.out.println("UserId = " + userId);
        if (update.callbackQuery().from().username() != null) {
            userId = "@" + update.callbackQuery().from().username();
            SendMessage message = new SendMessage(1281939927, "Вам необходимо связаться с пользователем " + userId + ". Ему требуется помощь");
            SendResponse response = telegramBot.execute(message);
        } else {
            SendMessage message = new SendMessage(1281939927, "Вам необходимо связаться с пользователем id " + userId + ". Ему требуется помощь");
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method generates respond with greeting message and calls main menu list by text message */
    private void callMainMenu(Update update, String state) {
        InlineKeyboardMarkup inLineKeyboardMainMenu = generateMainMenuList();
        if (state.equals("default")) {
            /* try to find out username to generate respectful greeting */
            String user = "Незнакомец";
            if (update.message().from().firstName() != null
                    && update.message().from().lastName() != null) {
                user = update.message().from().firstName() + " " + update.message().from().lastName();
            }
            if (update.message().from().firstName() != null
                    && update.message().from().lastName() == null) {
                user = update.message().from().firstName();
            }
            if (update.message().from().firstName() == null
                    && update.message().from().lastName() != null) {
                user = update.message().from().lastName();
            }
            if (update.message().from().firstName() == null
                    && update.message().from().lastName() == null) {
                if (update.message().from().username() != null) {
                    user = update.message().from().username();
                }
            }
            SendMessage message = new SendMessage(update.message().chat().id(),
                    user + ", добро пожаловать в чат-бот приюта домашних питомцев. Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardMainMenu);
            SendResponse response = telegramBot.execute(message);
        }
        if (state.equals("main_menu")) {
            SendMessage message = new SendMessage(update.message().chat().id(),
                    "Вы в главном меню. Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardMainMenu);
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method calls main menu list via CallbackQuery data */
    private void callMainMenu(Update update) {
        InlineKeyboardMarkup inLineKeyboardMainMenu = generateMainMenuList();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                "Вы в главном меню. Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardMainMenu);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method calls consultation menu list for a new user by text message */
    private void callNewUserConsultationMenu(Update update, String state) {
        if (state.equals("general_information")) {
            InlineKeyboardMarkup inLineKeyboardGeneralConsultationMenu = generateNewUserConsultationMenuList();
            SendMessage message = new SendMessage(update.message().chat().id(), "Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardGeneralConsultationMenu);
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method calls consultation menu list for a new user via CallbackQuery data */
    private void callNewUserConsultationMenu(Update update) {
        InlineKeyboardMarkup inLineKeyboardGeneralConsultationMenu = generateNewUserConsultationMenuList();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(), "Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardGeneralConsultationMenu);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method calls consultation menu list for a possible adoptive person (dog) by text message */
    private void callConsultationMenuForAdoptivePerson(Update update, String state) {
        if (state.equals("consultation_menu")) {
            InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateConsultationMenuForAdoptivePerson();
            SendMessage message = new SendMessage(update.message().chat().id(), "Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
            SendResponse response = telegramBot.execute(message);
        }
    }
    /* This method calls consultation menu list for a possible adoptive person (cat) by text message */
    private void callConsultationMenuForAdoptivePersonForCats(Update update, String state) {
        if (state.equals("consultation_menu_for_cat")) {
            InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePersonForCat = generateConsultationMenuForAdoptivePersonForCat();
            SendMessage message = new SendMessage(update.message().chat().id(), "Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePersonForCat);
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method calls consultation menu list for a possible adoptive person (dog) via CallbackQuery data */
    private void callConsultationMenuForAdoptivePerson(Update update) {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateConsultationMenuForAdoptivePerson();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(), "Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
        SendResponse response = telegramBot.execute(message);
    }
    /* This method calls consultation menu list for a possible adoptive person (cat)  via CallbackQuery data*/
    private void callConsultationMenuForAdoptivePersonForCats(Update update) {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePersonForCat = generateConsultationMenuForAdoptivePersonForCat();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(), "Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePersonForCat);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method calls report menu list by text message */
    private void callReportMenu(Update update, String state) {
        if (state.equals("report_menu")) {
            InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateReportMenu();
            SendMessage message = new SendMessage(update.message().chat().id(), "Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method calls report menu list via CallbackQuery data */
    private void callReportMenu(Update update) {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateReportMenu();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(), "Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
        SendResponse response = telegramBot.execute(message);
    }

    /* This method generates main menu list*/
    private InlineKeyboardMarkup generateMainMenuList() {
        InlineKeyboardMarkup inLineKeyboardMainMenu = new InlineKeyboardMarkup();
        inLineKeyboardMainMenu.addRow(new InlineKeyboardButton("Узнать информацию о приюте").
                callbackData("/main_menu_01"));
        inLineKeyboardMainMenu.addRow(new InlineKeyboardButton("Как взять собаку из приюта").
                callbackData("/main_menu_02"));
        inLineKeyboardMainMenu.addRow(new InlineKeyboardButton("Как взять кошку из приюта").
                callbackData("/main_menu_02_01"));
        inLineKeyboardMainMenu.addRow(new InlineKeyboardButton("Прислать отчет о питомце").
                callbackData("/main_menu_03"));
        inLineKeyboardMainMenu.addRow(new InlineKeyboardButton("Обратиться к волонтеру").
                callbackData("/call_volunteer"));
        return inLineKeyboardMainMenu;
    }

    /* This method generates consultation menu list for a new user*/
    private InlineKeyboardMarkup generateNewUserConsultationMenuList() {
        InlineKeyboardMarkup inLineKeyboardGeneralConsultationMenu = new InlineKeyboardMarkup();
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Получить общую информацию о приюте").
                callbackData("/menu_11"));
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Расписание работы, адрес, схема проезда к приюту").
                callbackData("/menu_12"));
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Получить общие рекомендации по технике безопасности на территории приюта").
                callbackData("/menu_13"));
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Зарегистрироваться").
                callbackData("/signup"));
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Вернуться в главное меню").
                callbackData("/main_menu"));
        inLineKeyboardGeneralConsultationMenu.addRow(new InlineKeyboardButton("Обратиться к волонтеру").
                callbackData("/call_volunteer"));
        return inLineKeyboardGeneralConsultationMenu;
    }

    /* This method generates consultation menu list for an adoptive person (dog)*/
    private InlineKeyboardMarkup generateConsultationMenuForAdoptivePerson() {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = new InlineKeyboardMarkup();
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Правила знакомства с собакой").
                callbackData("/menu_21"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Список документов, чтобы взять собаку из приюта").
                callbackData("/menu_22"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по транспортировке животного").
                callbackData("/menu_23"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для щенка").
                callbackData("/menu_24"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для взрослой собаки").
                callbackData("/menu_25"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для собаки с ограниченными возможностями").
                callbackData("/menu_26"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Советы кинолога по первичному общению с собакой").
                callbackData("/menu_27"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Список рекомендуемых кинологов для консультации").
                callbackData("/menu_28"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Причины отказа в заборе собаки из приюта").
                callbackData("/menu_29"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Зарегистрироваться").
                callbackData("/signup"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Вернуться в главное меню").
                callbackData("/main_menu"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Обратиться к волонтеру").
                callbackData("/call_volunteer"));
        return inLineKeyboardConsultationMenuForAdoptivePerson;
    }
    /* This method generates consultation menu list for an adoptive person (cat)*/
    private InlineKeyboardMarkup generateConsultationMenuForAdoptivePersonForCat() {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = new InlineKeyboardMarkup();
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Правила знакомства с кошкой").
                callbackData("/menu_121"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Список документов, чтобы взять кошку из приюта").
                callbackData("/menu_122"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по транспортировке животного").
                callbackData("/menu_123"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для котёнка").
                callbackData("/menu_124"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для взрослой кошки").
                callbackData("/menu_125"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Рекомендации по обустройтсву дома для кошки с ограниченными возможностями").
                callbackData("/menu_126"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Советы ветеринара по первичному общению с кошкой").
                callbackData("/menu_127"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Список рекомендуемых ветеринаров для консультации").
                callbackData("/menu_128"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Причины отказа в заборе кошки из приюта").
                callbackData("/menu_129"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Зарегистрироваться").
                callbackData("/signup"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Вернуться в главное меню").
                callbackData("/main_menu"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Обратиться к волонтеру").
                callbackData("/call_volunteer"));
        return inLineKeyboardConsultationMenuForAdoptivePerson;
    }

    /* This method generates report menu list */
    private InlineKeyboardMarkup generateReportMenu() {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = new InlineKeyboardMarkup();
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Получить форму ежедневного отчета").
                callbackData("/menu_31"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Направить ежедневный отчет").
                callbackData("/menu_32"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Вернуться в главное меню").
                callbackData("/main_menu"));
        inLineKeyboardConsultationMenuForAdoptivePerson.addRow(new InlineKeyboardButton("Обратиться к волонтеру").
                callbackData("/call_volunteer"));
        return inLineKeyboardConsultationMenuForAdoptivePerson;
    }
}
