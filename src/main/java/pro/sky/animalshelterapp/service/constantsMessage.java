package pro.sky.animalshelterapp.service;

public interface constantsMessage {

    //Общие команды

    String START_CMD = "/start";

    String GREETINGS_TEXT = "Приют - Бот в Астане, приветствует Вас!!! Выберите, какой приют вас интересует: ";

    String CHOOSE_OPTION = "Выберите пункт из перечня в меню: ";

    String CONTACT_ME_CMD = "Оставить контакты для связи \uD83D\uDCDE";
    String CONTACT_ME_TEXT = "Пожалуйста, отправьте свои данные в формате:\n" +
            "\nИмя, моб.телефон, email\n";

    String SUCCESS_SAVING_TEXT = "Контактные данные сохранены, с Вами свяжутся в ближайшее время.";

    String CALL_VOLUNTEER_CMD = "Позвать волонтера \uD83E\uDDD1";
    String CALL_VOLUNTEER_TEXT = "Волонтер скоро придёт на помощь!";
    Long VOLUNTEERS_CHAT_ID = -467355830L;

    String INVALID_NOTIFICATION_OR_CMD = "Бот не знает такой команды. \n\n Если вам требуется помощь, нажмите кнопку в меню \"Позвать волонтера\".";

    String REFUSAL_REASONS_CMD = "Причины отказа \uD83D\uDEAB";
    String REFUSAL_REASONS_TEXT = "Причины отказа в усыновлении питомца могут быть разными. Ниже приведены лишь некоторые самые частые. " +
            "Итак, отказ могут получить:\n" +
            "\n\uD83D\uDD34 Семьи с маленькими детьми, молодые родители-одиночки;\n" +
            "\n\uD83D\uDD34 Люди без опыта содержания животных;\n" +
            "\n\uD83D\uDD34 Люди, живущие на съемном жилье;\n" +
            "\n\uD83D\uDD34 Те, кто берет животное на подарок ребенку или члену семьи без его участия;\n" +
            "\n\uD83D\uDD34 Безработные;\n" +
            "\n\uD83D\uDD34 Те, кто безответственно относится к процессу взятия питомца из приюта.\n" +
            "\nЕсли после передачи питомца новым хозяевам выяснится, что они ввели сотрудников приюта в заблуждение (обманули), " +
            "то животное забирается обратно.";

    String DOCUMENTS_CMD = "Документы \uD83D\uDDC2";
    String DOCUMENTS_TEXT = "Список необходимых документов следующий:\n" +
            "\n\uD83D\uDFE2 паспорт;\n" +
            "\n\uD83D\uDFE2 местная регистрация;\n" +
            "\n\uD83D\uDFE2 документ, подтверждающий наличие собственного жилья;\n" +
            "\n\uD83D\uDFE2 согласие всех членов семьи, которые будут проживать с питомцем " +
            "(никто не хочет, чтобы от животного отказались из-за аллергии родственника).\n" +
            "\nПосле того как вы выбрали питомца и познакомились с ним, а также доказали, что ему будет с вами хорошо, " +
            "приют заключает с вами договор о передаче животного и об ответственном содержании.\n" +
            "\nЕсли обстоятельства не позволяют вам взять животное домой, можно стать волонтером, " +
            "помощь которых особенно требуется приютам.";

    String SEND_REPORT_CMD = "Прислать отчет \uD83D\uDCC4";
    String REPORT_FORM = "Пожалуйста, пришлите отчет в следующей форме: \n" +
            "\n\uD83D\uDFE2 Рацион питомца;\n" +
            "\n\uD83D\uDFE2 Общее самочувствие и привыкание к новому месту;\n" +
            "\n\uD83D\uDFE2 Изменение в поведении: отказ от старых привычек, приобретение новых\n" +
            "\n Затем отправьте фото животного. Одного фото будет достаточно:)";

    String PHOTO_REPORT_REQUIRED = "Отлично, текст отчета у нас уже есть.Теперь пришлите фото питомца.\n" +
            "\n❗ Внимание ❗ Без фото отчет не будет принят к рассмотрению!";

    String UNKNOWN_FILE = "Если вы хотите отправить отчет, сначала после нажатия кнопки меню \"Прислать отчет\" нужно отправить текст отчета!";

    String BAD_REPORT_WARNING = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
            "Пожалуйста, подойди ответственнее к этому занятию. " +
            "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного.";

    String TRIAL_PASSED = "Дорогой усыновитель! Мы поздравляем тебя с прохождением испытательного срока! " +
            "Теперь ты полноправный владелец питомца, который, мы надеемся, уже стал любимым членом твоей семьи.";

    String TRIAL_EXTENDED = "Дорогой усыновитель, твой испытательный срок продлен. " +
            "Кажется, не все твои отчеты были заполнены должным образом. " +
            "Пожалуйста, продолжай присылать их и отнесись к этому более ответственно. " +
            "Если у тебя есть вопросы, можешь связаться с волонтером. Новая дата окончания испытательного срока: ";

    String TRIAL_NOT_PASSED = "Дорогой усыновитель, к сожалению, ты не прошел испытательный срок. " +
            "Пожалуйста, собери питомца и его вещи и привези обратно в приют в течение 7 дней. " +
            "Если питомец не будет возвращен в течение указанного срока – это будет расценено как нарушение договора. " +
            "Если по какой-то причине вы не можете привезти питомца самостоятельно, пожалуйста, свяжитесь с волонтером.";

    //Команды для приюта собак

    String DOG_SHELTER_CMD = "Приют для собак \uD83D\uDC36";

    String DOG_SHELTER_INFO_CMD = "О приюте \uD83D\uDC36";

    String BACK_TO_CHOOSE_ANIMAL = "Назад к выбору питомца ↩";

    String DOG_ABOUT_US_CMD = "Кто мы \uD83E\uDDB8";
    String DOG_ABOUT_US_TEXT = "Приют был организован в 2013 году, через него прошло множество собак. " +
            "Сейчас в наш приют вмещает 30 четвероногих. Небольшое количество подопечных фонда является гарантией своевременного и " +
            "качественного ухода за собаками — их прививают, лечат, стерилизуют.";

    String DOG_WORKING_HOURS_CMD = "Наш адрес \uD83D\uDCCD";
    String DOG_WORKING_HOURS_TEXT = "Мы находимся по адресу: г. Нур-Султан, ул. Аккорган, д. 5В\n" +
            "\n\uD83D\uDFE2 Схема проезда: Автобус №8, №54 до остановки Болашак" +
            "\n\uD83D\uDFE2 Режим работы приюта: с Пн по Вс с 09:00 до 18:00 ";


    String DOG_SAFETY_RECOMMENDATION_CMD = "Безопасность на территории приюта ❗";
    String DOG_SAFETY_RECOMMENDATION_TEXT = "\uD83D\uDD34 На территории приюта просим не кричать, не размахивать руками, не бегать между будками или вольерами, не пугать и не дразнить животных.\n" +
            "\n\uD83D\uDD34 Если у вас есть свои домашние питомцы, и они случайно заболели (инфекция), пожалуйста, воздержитесь от приезда в приют.\n" +
            "\n\uD83D\uDD34 Запрещается самостоятельно открывать выгулы и вольеры без разрешения сотрудников приюта.\n" +
            "\n\uD83D\uDD34 Запрещается посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
            "\nПри несоблюдении правил, сотрудники приюта оставляют за собой право отказать вам в посещении приюта.";

    String DOG_SECURITY_CONTACT_CMD = "Оформление пропуска \uD83D\uDCDD";
    String DOG_SECURITY_CONTACT_TEXT = "\uD83D\uDFE2 Для того, чтобы оформить пропуск на машину для въезда на территорию приюта, " +
            "вам нужно связаться с сотрудником охраны по тел. +7 (777) 225 2325";

    String BACK_TO_DOG_START_MENU_CMD = "Назад ↩";

    String HOW_TO_TAKE_DOG_CMD = "Как забрать собаку ℹ";

    String MEET_THE_DOG_CMD = "Знакомство с собакой \uD83D\uDC36";
    String MEET_THE_DOG_TEXT = "\uD83D\uDFE2 Мы рекомендуем начать процесс знакомства и общения с будущим подопечным заранее.\n" +
            "\n\uD83D\uDFE2 После того, как вы сделали выбор, начните навещать животное в приюте, строить с ним доверительные отношения: " +
            "приносить собаке лакомства, начать выводить её на прогулки, аккуратно гладить. " +
            "Это должно происходить спокойно и ненавязчиво, без какого-либо давления с вашей стороны.\n" +
            "\n\uD83D\uDFE2 Когда животное начнёт вас узнавать, вилять хвостом при встрече, и позволит с ним играть, " +
            "можно устроить пару гостевых посещений, приведя собаку в дом. " +
            "Это поможет собаке в дальнейшем более легкому знакомству с незнакомой обстановкой и привыканию к новому дому.";

    String DOG_TRANSPORTING_AND_ADVICE_CMD = "Перевозка собаки и обустройство \uD83D\uDE99\uD83C\uDFE1";
    String BACK_TO_DOG_RECOMMENDATION_MENU_CMD = "Назад к рекомендациям \uD83D\uDC15";

    String DOG_TRANSPORTING_CMD = "Перевозка собаки \uD83D\uDE97";
    String DOG_TRANSPORTING_TEXT = "\uD83D\uDFE2 Для адаптации собаки к переезду позвольте животному заранее познакомиться с контейнером " +
            "для перевозки и автомобилем, в котором пройдёт дорога домой.\n" +
            "\n\uD83D\uDFE2 Заранее приучите собаку к наморднику – " +
            "для этого, посещая приют, вкладывайте лакомства в этот аксессуар, используйте его как миску.\n" +
            "\n\uD83D\uDFE2 Через некоторое время можно начать надевать намордник на собаку перед началом прогулки, и снимать через некоторое время. \n" +
            "\n\uD83D\uDFE2 Если у вас нет своего транспорта, постарайтесь договориться с кем-то из друзей или знакомых. " +
            "Везти собаку домой на общественном транспорте – не самая лучшая идея.";

    String DOG_ADVICE_CMD = "Обустройство дома для собаки \uD83D\uDECC";
    String BACK_TO_DOG_TRANSPORT_AND_ADVICE_MENU_CMD = "Назад ⬅\uD83D\uDC15";

    String DOG_COMMON_ADVICE_TEXT = "\uD83D\uDFE2 Прежде чем вы привезете нового члена семьи в дом, позаботьтесь о том, чтобы купить средство от блох и клещей, " +
            "зоошампунь и корм.\n" +
            "\n\uD83D\uDFE2До того, как купить еду для питомца, посоветуйтесь с сотрудниками приюта или куратором животного. " +
            "Возможно, у него назначен специализированный корм или имеется аллергия.\n" +
            "\n\uD83D\uDFE2 Также для собаки следует приобрести две миски (одна для еды, другая для воды), ошейник, шлейку, поводок, намордник, " +
            "адресник, на котором будет кличка питомца и контактные данные владельца, игрушки, лежанку, вольер, впитывающие пеленки — " +
            "некоторые приютские собаки не приучены к выгулу и могут по-началу ходить в туалет дома, не смотря на свой возраст " +
            "и возможный прошлый опыт проживания в квартире. \n\n\uD83D\uDFE2 Желательно спрятать все провода, до которых может дотянуться питомец, " +
            "ограничить доступ к бытовой химии, продуктам и растениям, купить мусорное ведро с плотной крышкой." +
            "\n" +
            "\nДля более подробной информации, выберите один из следующих пунктов: ";

    String ADVICE_FOR_PUPPY_CMD = "Щенок \uD83D\uDC36";
    String ADVICE_FOR_PUPPY_TEXT = "\uD83D\uDFE2 Помимо общих рекомендаций обратите внимание на места в квартире/доме, где уголки обоев отходят от стены, " +
            "то же самое касается неплотно прилегающего линолеума и прочих отделочных материалов. Все уголки нужно тщательно проклеить, " +
            "чтобы щенок не начал их грызть и случайно не подавился.\n" +
            "\n\uD83D\uDFE2 Объясните всем членам семьи (особенно детям), что теперь нельзя оставлять вещи и мелкие предметы на видном месте – " +
            "щенок может испортить одежду или проглотить несъедобный предмет. \n\n\uD83D\uDFE2 Любые мелкие и хрупкие предметы " +
            "нужно хранить в недосягаемости для щенка.\n" +
            "\n\uD83D\uDFE2 Теперь обувь придется спрятать в тумбочку, а домашние тапочки хранить на отдельной полке, иначе они рискуют быть погрызанными.\n" +
            "\n\uD83D\uDFE2 Обязательно сделайте генеральную уборку за день до приезда щенка, вымойте полы, отодвиньте шкафы и диваны – " +
            "под ними могут валяться опасные для щенка предметы (пуговицы, мелкие игрушки, скрепки и т.п.).";

    String ADVICE_FOR_ADULT_DOG_CMD = "Взрослая собака \uD83D\uDC15";
    String ADVICE_FOR_ADULT_DOG_TEXT = "\uD83D\uDFE2 В зависимости от размера вашего будущего питомца потребуется приобрести вольер или " +
            "установить ограждение в квартире. \n" +
            "\n\uD83D\uDFE2 Лежанка должна подходить по размеру: измерьте собаку от носа до хвоста и от лопаток до кончиков лап, " +
            "прибавьте 10-15 см и получите подходящий размет спального места для вашего нового друга.\n" +
            "\n\uD83D\uDFE2 Если в доме есть другие питомцы, имейте в виду, что знакомить с ними нового члена семьи сразу нельзя. " +
            "Первые несколько дней собака из приюта должна привыкать к новому месту и только потом знакомиться с новыми животными.";

    String ADVICE_FOR_SPECIAL_DOG_CMD = "Собака с ОВЗ \uD83D\uDC36❗";
    String ADVICE_FOR_SPECIAL_DOG_TEXT = "\uD83D\uDFE2 Собаки с ограниченными возможности особенно нуждаются во внимании, но пусть это вас не пугает. " +
            "Порой слепую или глухую собаку не отличить по поведению от полностью здоровой, а песик без лапки может бегать не хуже четырехногого.\n" +
            "\n\uD83D\uDFE2 Недостаток одной лапы собаки компенсируют довольно быстро, научившись ходить и даже бегать на трех ногах. \n" +
            "Длительные прогулки, активные виды спорта и прыжки в высоту под запретом для таких питомцев. Однако не надо впадать в крайность. " +
            "Недостаток движения и обильная калорийная пища могут привести к ожирению и болезням внутренних органов у животного.\n" +
            "\n\uD83D\uDFE2 Еще одной достаточно распространенной проблемой является потеря зрения или слуха животного. Чаще всего это происходит " +
            "в старости, но иногда, в случае травмы или врожденных патологий, этими недугами страдают и достаточно молодые животные.\n " +
            "\n\uD83D\uDFE2 Лишившись одного из органов чувств, животное, как правило, компенсирует недостаток. Так слепые собаки привыкают ориентироваться " +
            "на нюх и память, а те, кто плохо слышат — отлично различают вибрацию и внимательно реагируют на любые изменения " +
            "в окружающем пространстве. \n\n\uD83D\uDFE2 Соблюдение несложных правил техники безопасности и необходимость гулять с такой собакой " +
            "только на поводке — вот, пожалуй, и все ограничения, накладываемые в подобной ситуации.";

    String CYNOLOGIST_CMD = "Помощь кинолога \uD83C\uDD98";
    String BACK_TO_CYNOLOGIST_MENU_CMD = "Назад к советам кинолога⬅";

    String CYNOLOGIST_ADVICE_CMD = "Советы кинолога ℹ";
    String CYNOLOGIST_ADVICE_TEXT = "\uD83D\uDD34 Не навязывайте собаке своё общество. Разумеется, необходимо следить за животным и находиться рядом " +
            "с ним, но нельзя забывать, что излишнее внимание и активное взаимодействие с питомцем принесёт ему лишний стресс, " +
            "которого так важно избегать на первых порах. В идеале стоит дождаться момента, когда собака сама начнёт проявлять к вам интерес, " +
            "сделает первый шаг. Когда питомец наконец пойдёт на контакт, рекомендуется поощрить это действие поглаживанием или лакомством.\n" +
            "\n\uD83D\uDD34 Не мешайте животному самостоятельно исследовать новое окружение. Пусть собака обойдёт весь дом, понюхает каждый предмет " +
            "и угол, убедится в том, что жилище для неё безопасно и не представляет угрозы.\n" +
            "\n\uD83D\uDD34 Не устраивайте “смотрины” или какие-либо сборы гостей в первые недели жизни с животным. Собака может запаниковать " +
            "и начать проявлять агрессию из-за внезапного появления шумных компаний незнакомых людей.\n" +
            "\n\uD83D\uDD34 Не торопитесь ухаживать за собакой — кормить её, поить водой, гладить или затаскивать в ванну. Перед осуществлением " +
            "этих действий нужно установить доверительные, непринуждённые отношения с животным. Продемонстрируйте питомцу миски " +
            "для корма и воды и просто оставьте их на своём месте. Через некоторое время, возможно, даже пару дней, собака сама начнёт " +
            "есть из них. Не стойте над животным во время еды, не тревожьте его сон.\n" +
            "\n\uD83D\uDD34 Если вы столкнулись со страхами собаки, действуйте спокойно и ласково, не напрягая своего питомца. К примеру, в случае " +
            "с боязнью лифтов дайте животному некоторое время на наблюдение за лифтом, постепенно увеличивая длительность " +
            "таких “сеансов”. Если собака начинает вести себя спокойно, поощряйте это поведение едой и поглаживаниями. Следующий шаг — " +
            "“обнаружение” в лифте знакомого человека, готового поощрить собаку лакомством за уверенное поведение. " +
            "Аналогичные схемы можно применить в отношении любого страха или неуверенности питомца; главное — взаимодействовать с собакой " +
            "без суеты, спокойно и поэтапно, предоставляя ей необходимое для привыкания время.";

    String CYNOLOGIST_CONTACTS_CMD = "Контакты кинологов ☎";
    String CYNOLOGIST_CONTACTS_TEXT = "Контакты проверенных кинологов:\n" +
            "https://k-9.kz/\n" +
            "https://www.olx.kz/d/obyavlenie/kinolog-dressirovka-sobak-IDl2fIo.html#d5d8765dd2\n" +
            "https://www.olx.kz/d/obyavlenie/dressirovka-sobak-kinolog-nur-sultan-astana-IDjp6gY.html#d5d8765dd2";


    // Команды для приюта кошек

    String CAT_SHELTER_CMD = "Приют для кошек \uD83D\uDC31";

    String CAT_SHELTER_INFO_CMD = "О приюте \uD83D\uDC31";

    String CAT_ABOUT_US_CMD = "Кто мы \uD83D\uDC50";
    String CAT_ABOUT_US_TEXT = "Наш приют существует уже более 5 лет. Команда волонтеров старается сделать всё от них зависящее, " +
            "чтобы все коты, кошки и котята чувствовали в стенах приюта малую частичку дома.\n" +
            "\nОднако мы не можем заменить им дом и заботливых хозяев в полной мере. Вы же можете подарить живому существу тёплый дом, любовь и ласку.\n" +
            "\nМы с удовольствием ответим на все Ваши вопросы, " +
            "и поможем подготовиться к жизни с новым другом.";

    String CAT_WORKING_HOURS_CMD = "Наш адрес \uD83D\uDDFA";
    String CAT_WORKING_HOURS_TEXT = "Мы находимся по адресу: г. Нур-Султан, ул. Абая, д. 40\n" +
            "\n\uD83D\uDFE2 Схема проезда: Автобус №21, №7 до остановки Мегаспорт" +
            "\n\uD83D\uDFE2 Режим работы приюта: с Пн по Вс с 09:00 до 18:00";


    String CAT_SAFETY_RECOMMENDATION_CMD = "Безопасность на территории приюта ⚠";
    String CAT_SAFETY_RECOMMENDATION_TEXT = "\uD83D\uDD34 При посещении приюта запрещается гладить кошек через сетку вольера.\n" +
            "\n\uD83D\uDD34 Если у вас есть свои домашние питомцы, и они случайно заболели (инфекция), пожалуйста, воздержитесь от посещения в приют.\n" +
            "\n\uD83D\uDD34 Запрещается самостоятельно заходить в кошатник без разрешения сотрудников приюта.\n" +
            "\n\uD83D\uDD34 Запрещается посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
            "\nПри несоблюдении вами правил сотрудники приюта оставляют за собой право отказать вам в посещении приюта.";

    String CAT_SECURITY_CONTACT_CMD = "Оформление пропуска \uD83D\uDCCB";
    String CAT_SECURITY_CONTACT_TEXT = "\uD83D\uDFE2 Для того, чтобы оформить пропуск на машину для въезда на территорию приюта, " +
            "вам нужно связаться с сотрудником охраны по тел. +7 (777) 432 4532";

    String BACK_TO_CAT_START_MENU_CMD = "Назад ⬅";

    String HOW_TO_TAKE_CAT_CMD = "Как забрать кошку ℹ";

    String MEET_THE_CAT_CMD = "Знакомство с кошкой \uD83D\uDC31";
    String MEET_THE_CAT_TEXT = "\uD83D\uDFE3 Прежде чем определиться с выбором кошки, посетите приют несколько раз, познакомитесь с местными обитателями. " +
            "Понаблюдайте за ними: у кого какой характер, повадки и особенности поведения.\n" +
            "\n\uD83D\uDFE3 При выборе питомца учитывайте и свой образ жизни. Например, если вы постоянно работаете или много путешествуете, " +
            "то лучше завести независимую кошку, не склонную к беспокойству или нервному поведению. " +
            "Маленький котенок – не вариант, так как за ним нужно много ухаживать и заботиться.\n" +
            "\n\uD83D\uDFE3 Домоседам подойдет ласковый и энергичный питомец, который сможет развлекать вас в свободное время.\n" +
            "\n\uD83D\uDFE3 Если у вас есть дети, то будущий пушистый друг должен быть добрым и дружелюбным, чтобы выдержать капризы и игры ребят.\n" +
            "\n\uD83D\uDFE3 Когда какая-то кошечка вам приглянется больше других, принесите для нее лакомство, угостите, предложите ей поиграть с вами. " +
            "Только ни в коем случае нельзя быть настойчивым! Кошка сама должна проявить к вам интерес и позволить погладить ее.";

    String CAT_TRANSPORTING_AND_ADVICE_CMD = "Перевозка кошки и обустройство \uD83D\uDE99\uD83C\uDFE1";
    String BACK_TO_CAT_RECOMMENDATION_MENU_CMD = "Назад к рекомендациям \uD83D\uDC08";

    String CAT_TRANSPORTING_CMD = "Транспортировка кошки \uD83D\uDE97";
    String CAT_TRANSPORTING_TEXT = "\uD83D\uDFE3 Когда вы определились, какого котика хотите забрать домой, купите и принесите переноску в приют заранее. " +
            "Оставьте ее в зоне видимости выбранного животного, чтобы оно могло все осмотреть и понюхать.\n " +
            "\n\uD83D\uDFE3 Положите лакомство внутрь переноски, чтобы котик сам в нее заходил. " +
            "Можно положить внутрь любимую игрушку или плед, на котором животное спит. \n" +
            "\n\uD83D\uDFE3 Когда кот привыкнет, можно будет отправиться с ним домой. \n" +
            "\n\uD83D\uDFE3 Ни в коем случае не пытайтесь силой затолкать животное внутрь переноски!\n" +
            "\n\uD83D\uDFE3 Для перевоза животного домой рекомендуется использовать автомобиль. " +
            "Если у вас или ваших друзей нет такой возможности, вызовите такси. " +
            "Общественный транспорт - не лучший выбор в данной ситуации.";

    String CAT_ADVICE_CMD = "Обустройство дома для кошки \uD83D\uDECC";
    String BACK_TO_CAT_TRANSPORT_AND_ADVICE_MENU_CMD = "Назад ⬅\uD83D\uDC08";

    String CAT_COMMON_ADVICE_TEXT = "\uD83D\uDFE3 Прежде чем вы привезете нового члена семьи в дом, позаботьтесь о том, чтобы купить средство от блох и клещей, " +
            "зоошампунь и корм. \n\n\uD83D\uDFE3 До того, как купить еду для питомца, посоветуйтесь с сотрудниками приюта или куратором животного. " +
            "Возможно, у него назначен специализированный корм или имеется аллергия.\n" +
            "\n\uD83D\uDFE3 Также для кошки следует приобрести несколько мисок (для еды и для воды, мисок для воды должно быть несколько " +
            "и их нужно расставить в разных частях дома), когтеточки, игрушки, лежанку, лоток, наполнитель, продумать возможность для кошки " +
            "залезать наверх, сделать специальные полки на стене или купить комплекс с домиком и полками на разной высоте.\n" +
            "\n\uD83D\uDFE3 Желательно спрятать все провода, до которых может дотянуться питомец, " +
            "ограничить доступ к бытовой химии, продуктам и растениям, купить мусорное ведро с плотной крышкой, установить на окна сетку-антикошку.\n" +
            "\nДля более подробной информации выберите один из следующих пунктов: ";

    String ADVICE_FOR_KITTEN_CMD = "Котенок \uD83D\uDC31";
    String ADVICE_FOR_KITTEN_TEXT = "Котята гораздо более активные и любопытные, чем взрослые животные. " +
            "К тому же они гораздо меньше размером. Котенка легко потерять даже в однокомнатной квартире.\n " +
            "\nВот несколько советов для подготовки дома к появлению котенка:\n" +
            "\n\uD83D\uDFE3 Поставьте ограничители на двери, закрывайте шкафы, проверяйте стиральную машину перед загрузкой белья.\n" +
            "\n\uD83D\uDFE3 Держите окна и дверь на балкон закрытыми, чтобы котенок не выпал.\n" +
            "\n\uD83D\uDFE3 Нужно постоянно следить за питомцем, когда в доме работает обогреватель и плита.\n" +
            "\n\uD83D\uDFE3 Уберите повыше мелкие и хрупкие предметы.\n" +
            "\n\uD83D\uDFE3 Уберите из дома ядовитые растения, если такие имеются.\n" +
            "\n\uD83D\uDFE3 Будьте готовы к тому, что котенок практически постоянно будет требовать вашего внимания.";

    String ADVICE_FOR_ADULT_CAT_CMD = "Взрослая кошка \uD83D\uDC08";
    String ADVICE_FOR_ADULT_CAT_TEXT = "\uD83D\uDFE3 Первое время не мешайте кошке спокойно осматривать дом, не ходите за ней по пятам," +
            " не нервируйте постоянным присутствием рядом. Если вы переживаете, что она может что-то разбить, " +
            "уронить, испортить, уберите эти предметы подальше и позвольте животному спокойно обследовать новую территорию.\n" +
            "\n\uD83D\uDFE3 Обязательно заберите из приюта какие-то личные вещи кошки. Это поможет ей быстрее адаптироваться.\n " +
            "\n\uD83D\uDFE3 Любимая игрушка, плед или лежанка, возможно, коробка... Все пригодится для более спокойного привыкания питомца к новому месту.";

    String ADVICE_FOR_SPECIAL_CAT_CMD = "Кошка с ОВЗ \uD83D\uDC31❗";
    String ADVICE_FOR_SPECIAL_CAT_TEXT = "Взять из приюта котика с ограниченными возможностями – это серьезное решение. " +
            "Однако пусть это вас не отпугнет, ведь такие животные нуждаются в любви и заботе чуть больше остальных.\n" +
            "\n\uD83D\uDFE3 Глухие кошки обычно ведут себя подобно слышащим сородичам. Они не слышат, но при этом прекрасно улавливают вибрации и колебания. " +
            "Однако учитывайте, что ваше бесшумное появление в комнате напугает животное. Поэтому чтобы кошка не испугалась от вашего приближения, " +
            "начинайте сильнее топать издалека. Не заходите к ней со спины – старайтесь, чтобы перед приближением вы попадали в поле зрения питомицы.\n " +
            "\n\uD83D\uDFE3 Так же ведите себя, если намерены погладить. Кошка должна увидеть руку, которая приближается к ней и только потом ощутить прикосновение.\n" +
            "\n\uD83D\uDFE3 Слепые кошки компенсируют отсутствие зрения слухом и обонянием. Не подкрадывайтесь к кошке сзади, не кричите, избегайте резких звуков, " +
            "чтобы не пугать животное. \n\n\uD83D\uDFE3Больше разговаривайте с питомцем, ласково зовите по имени, прежде чем подойти, " +
            "давайте понюхать руку до того, как погладить.\n" +
            "\n\uD83D\uDFE3 Недостаток одной лапы кошки компенсируют довольно быстро, научившись ходить и даже бегать на трех ногах. " +
            "Однако, если вы решились взять животное с парализованными задними лапами, побеспокойтесь о том, " +
            "чтобы у вам дома не было высоких порогов и лестниц. С таким животным надо будет чаще посещать ветеринара, " +
            "уделять котику больше внимания и заботы. Будьте уверены, питомец ответит вам взаимностью.";

    String FELINOLOGIST_CMD = "Помощь фелинолога \uD83C\uDD98";
    String BACK_TO_FELINOLOGIST_MENU_CMD = "Назад к советам фелинолога ⬅";

    String FELINOLOGIST_ADVICE_CMD = "Советы фелинолога ℹ";
    String FELINOLOGIST_ADVICE_TEXT = "\uD83D\uDFE3 Любая кошка обладает определенным характером. И зачастую его становление зависит от того, " +
            "как вы будете с ней обращаться. Кошки чувствуют любую нотку агрессии в свою сторону. И для того, чтобы найти с питомцем общий язык, " +
            "необходимо с момента знакомства относиться к нему с лаской и добротой.\n" +
            "\n\uD83D\uDFE3 Ни в коем случае кошку нельзя ругать, бить. Если кошка напакостила или залезла, куда нельзя, не надо сразу бежать к ее сторону, размахивая руками.\n" +
            "\n\uD83D\uDFE3 С самых первых дней пребывания кошки в доме чаще хвалите своего питомца и гладьте, если он позволяет.\n" +
            "\n\uD83D\uDFE3 Не перекармливайте кота. Заранее узнайте у куратора, в каком режиме стоит кормить животное.\n" +
            "\n\uD83D\uDFE3 Расставьте по дому побольше мисок с водой. Как правило, кошки не пьют там, где едят, " +
            "поэтому нет смысла ставить воду возле корма. Источники воды не должны стоять на проходе или посреди открытого пространства. " +
            "Когда кошки пьют, они более беззащитные чем обычно.";

    String FELINOLOGIST_CONTACTS_CMD = "Контакты фелинологов ☎";
    String FELINOLOGIST_CONTACTS_TEXT = "Контакты проверенных фелинологов:\n" +
            "https://www.kuf.kz/\n" +
            "https://planetakoshek.kz/\n" +
            "https://wacc-cats.org/";

}

