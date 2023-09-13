package ru.daniil.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.daniil.telegrambot.models.Messages;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.repository.MessageRepository;

@Service
@Component
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void createMessage(Message infoMessage, User user, SendMessage answer) {
        Messages messages = new Messages();
        messages.setId(infoMessage.getMessageId());
        messages.setUser(user);
        messages.setQuestion(infoMessage.getText());
        messages.setAnswer(answer.getText());
        messageRepository.save(messages);
    }
}