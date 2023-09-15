package ru.daniil.telegrambot.components;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@Component
@Slf4j
public class SendMessageComponent  {
    private final SendMessage sendMessage;

    public SendMessageComponent() {
        sendMessage = new SendMessage();
    }

    public void createSendMessage(long chatId, String message) {
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        log.info("The message sent by the bot has been saved");
    }
}
