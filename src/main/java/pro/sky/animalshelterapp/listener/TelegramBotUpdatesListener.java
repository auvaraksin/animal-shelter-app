package pro.sky.animalshelterapp.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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

            switch (update.message().text()) {
                case "/start":
                    generateMainMenu(update);
                    break;
                default: generateMainMenu(update);
                    break;
            }
            ;
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /*
        This method generates respond with greeting message and main menu list via direct command "/start"
        */
    private void generateMainMenu(Update update) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.addRow(new InlineKeyboardButton("Узнать информацию о приюте").callbackData("/main_menu_01"));
        markupInLine.addRow(new InlineKeyboardButton("Как взять собаку из приюта").callbackData("/main_menu_02"));
        markupInLine.addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/main_menu_03"));
        markupInLine.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer"));
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Добро пожаловать в чат-бот приюта домашних питомцев. Выберите интересующий вас пункт меню")
                .replyMarkup(markupInLine);
        SendResponse response = telegramBot.execute(message);
    }
}
