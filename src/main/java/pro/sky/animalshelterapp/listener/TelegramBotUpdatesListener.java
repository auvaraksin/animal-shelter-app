package pro.sky.animalshelterapp.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.exeptions.WrongDataSavingExeption;
import pro.sky.animalshelterapp.interfaces.ClientService;
import pro.sky.animalshelterapp.interfaces.MessageSourceService;
import pro.sky.animalshelterapp.models.Client;
import pro.sky.animalshelterapp.models.Report;
import pro.sky.animalshelterapp.repositories.ReportRepository;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.datical.liquibase.ext.init.InitProjectUtil.getExtension;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    /* variable 'state' provides exchange rules of text messages with telegram-bot */
    private String state = "default";
    //@Value("${animal-shelter-app.photo.dir.path}")
    //private String photoDir;
    private final MessageSourceService messageSourceService;

    private final ReportRepository reportRepository;
    private final ClientService clientService;

    public TelegramBotUpdatesListener(MessageSourceService messageSourceService, ClientService clientService, ReportRepository reportRepository) {
        this.messageSourceService = messageSourceService;
        this.clientService = clientService;
        this.reportRepository = reportRepository;
    }

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
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
        Report report = new Report();
        if (update.message().photo() == null) {
            report.setText(update.message().text());
        }
        if (update.message().caption() == null) {
            //здесь должен быть метод чтения фотографии и записи её в БД
        }
        //Здесь должен быть метод записи текста и фотографии в БД
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Отчёт принят");
        SendResponse response = telegramBot.execute(message);
        //Метод скачивания файла

//        GetFile getFileRequest = new GetFile(update.message().photo()[0].fileId());
//        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
//        File file = getFileResponse.file();
//        byte[] fileContent = telegramBot.getFileContent(file);

        /*String fileId = photo.fileId();
        GetFile request = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(request);
        reportRepository.save(report);*/
        // String fullPath = telegramBot.getFullFilePath(file);
        //BufferedImage image = ImageIO.read(new java.io.File(fullPath));
        //ImageIO.write(image, "jpg",new java.io.File("photo/"+fileId+".jpg"));
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

    /* This method calls consultation menu list for a possible adoptive person by text message */
    private void callConsultationMenuForAdoptivePerson(Update update, String state) {
        if (state.equals("consultation_menu")) {
            InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateConsultationMenuForAdoptivePerson();
            SendMessage message = new SendMessage(update.message().chat().id(), "Выберите интересующий вас пункт меню")
                    .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
            SendResponse response = telegramBot.execute(message);
        }
    }

    /* This method calls consultation menu list for a possible adoptive person via CallbackQuery data */
    private void callConsultationMenuForAdoptivePerson(Update update) {
        InlineKeyboardMarkup inLineKeyboardConsultationMenuForAdoptivePerson = generateConsultationMenuForAdoptivePerson();
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(), "Выберите интересующий вас пункт меню")
                .replyMarkup(inLineKeyboardConsultationMenuForAdoptivePerson);
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

    /* This method generates consultation menu list for an adoptive person */
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
