package pro.sky.animalshelterapp.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

            /*
            Generate respond with greeting message via direct command "/start"
             */
            generateGreetingMessage(updates);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /*
        This method generates respond with greeting message via direct command "/start"
        */
    private void generateGreetingMessage(List<Update> updates) {
        updates
                .stream()
                .map(Update::message)
                .filter(message -> message.text().equals("/start"))
                .map(Message::chat)
                .map(Chat::id)
                .forEach(chat -> {
                    SendMessage message = new SendMessage(chat, "You are welcome to the animal shelter service chat room");
                    SendResponse response = telegramBot.execute(message);
                });
    }
}
