package ru.daniil.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.daniil.telegrambot.models.Message;
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

    public void createMessage(org.telegram.telegrambots.meta.api.objects.Message infoMessage) {
        Message message = new Message();
        message.setId(infoMessage.getMessageId());
        // message.setUser();
        message.setQuestion(infoMessage.getText());
        // message.setAnswer();
        messageRepository.save(message);
    }
}