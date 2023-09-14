package ru.daniil.telegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.service.DomainService;
import ru.daniil.telegrambot.service.UserService;

@Component
public class DomainScheduled {
    private final DomainService domainService;
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Autowired
    public DomainScheduled(DomainService domainService,
                           UserService userService,
                           TelegramBot telegramBot)
    {
        this.domainService = domainService;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void createDomain() {
        domainService.createDomain();
        mailingDomain();
    }

    private void mailingDomain() {
        var users = userService.getAllUser();
        for (User user : users) {
            telegramBot.sendMessage(domainService.mailingDomain(), user.getChatId());
        }
    }
}
