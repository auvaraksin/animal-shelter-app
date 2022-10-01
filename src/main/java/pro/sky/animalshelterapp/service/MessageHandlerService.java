package pro.sky.animalshelterapp.service;


import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;

public interface MessageHandlerService {
    void handleMessage(Message inputMessage, long chatId);

    void sendMessage(Long chatId, String inputMessage, Keyboard keyboard);

    void sendMessage(Long chatId, String inputMessage);

    //Обработка сообщений

}
