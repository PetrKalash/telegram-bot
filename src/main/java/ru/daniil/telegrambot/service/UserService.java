package ru.daniil.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Component
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(Message message) {
        if (userRepository.findById(message.getChatId()).isPresent()) return;

        User user = new User();
        user.setChatId(message.getChatId());
        user.setFirstName(message.getChat().getFirstName());

        Timestamp ts = new Timestamp(message.getDate());
        user.setLastMessageAt(new Date(ts.getTime()));

        userRepository.save(user);
        log.info("User save: " + user);
    }
}