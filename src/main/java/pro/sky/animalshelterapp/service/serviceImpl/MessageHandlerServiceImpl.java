package pro.sky.animalshelterapp.service.serviceImpl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.animalshelterapp.listener.TelegramBotUpdatesListener;
import pro.sky.animalshelterapp.service.ChatUserService;
import pro.sky.animalshelterapp.service.MessageHandlerService;
import pro.sky.animalshelterapp.service.ReportService;
import static pro.sky.animalshelterapp.service.MessageConstants.START_CMD;

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

                // Все кнопки

                case START_CMD:
            }
        }
    }

    @Override
    public void sendMessage(Long chatId, String inputMessage, Keyboard keyboard) {

    }

    @Override
    public void sendMessage(Long chatId, String inputMessage) {

    }
}