package pro.sky.animalshelterapp.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import com.pengrad.telegrambot.model.Message;
import pro.sky.animalshelterapp.service.ChatUserService;
import pro.sky.animalshelterapp.service.MessageHandlerService;
import pro.sky.animalshelterapp.service.ReportService;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    private final TelegramBot animalShelterBot;
    private final ChatUserService chatUserService;
    private final ReportService reportService;


    private final MessageHandlerService messageHandler;

    public TelegramBotUpdatesListener(TelegramBot animalShelterBot,
                                           ChatUserService chatUserService,
                                           ReportService reportService,
                                           MessageHandlerService messageHandler) {
        this.animalShelterBot = animalShelterBot;
        this.chatUserService = chatUserService;
        this.reportService = reportService;
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    public void init() {
        animalShelterBot.setUpdatesListener(this);
    }

    /**
     * Проверяет и обрабатывает обновление чата;
     * @param обновления используются для получения и проверки
     * @return подтверждает все обновления
     */

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            // check if the update has a message and message has text
            if (message != null) {
                messageHandler.handleMessage(message, extractChatId(message));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private long extractChatId(Message message) {
        return message.chat().id();
    }
}
