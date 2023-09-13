package ru.daniil.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.repository.UserRepository;

import java.util.Date;

@Component
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(Message message) {
        User user = new User();
        user.setChatId(message.getChatId());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastMessageAt(new Date());
        userRepository.save(user);
        log.info("User save: " + user);
    }

    public Iterable<User> getAllUser() {
        log.info("All users have been sent");
        return userRepository.findAll();
    }

    public User getUser(Long chatId) {
        log.info("The user with the ID was sent");
        return userRepository.findById(chatId).orElse(null);
    }
}