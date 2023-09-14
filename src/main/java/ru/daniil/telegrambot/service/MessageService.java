package ru.daniil.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.daniil.telegrambot.models.Messages;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.repository.IMessageRepository;

@Component
@Slf4j
public class MessageService {
    private final IMessageRepository IMessageRepository;

    @Autowired
    public MessageService(IMessageRepository IMessageRepository) {
        this.IMessageRepository = IMessageRepository;
    }

    public void createMessage(Message message, User user, SendMessage answer) {
        Messages messages = new Messages();
        messages.setId(message.getMessageId());
        messages.setUser(user);
        messages.setQuestion(message.getText());
        messages.setAnswer(answer.getText());
        saveMessage(messages);
    }

    public void saveMessage(Messages messages) {
        IMessageRepository.save(messages);
        log.info("The message was saved in the database");
    }
}