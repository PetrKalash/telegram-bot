package ru.daniil.telegrambot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.daniil.telegrambot.components.BotCommands;
import ru.daniil.telegrambot.config.BotConfig;
import ru.daniil.telegrambot.models.User;
import ru.daniil.telegrambot.service.DomainService;
import ru.daniil.telegrambot.service.MessageService;
import ru.daniil.telegrambot.service.UserService;

@Slf4j
@Component
@Service
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    private final BotConfig botConfig;
    private final UserService userService;
    private final MessageService messageService;
    private final DomainService domainService;

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       UserService userService,
                       MessageService messageService,
                       DomainService domainService) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.messageService = messageService;
        this.domainService = domainService;
        try {
            this.execute(new SetMyCommands(BOT_COMMAND_LIST, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = 0;
        String firstName = "";
        String receivedMessage = "";

        Message infoMessage = update.getMessage();
        if (update.hasMessage() && infoMessage.hasText()) {
            chatId = infoMessage.getChatId();
            firstName = infoMessage.getChat().getFirstName();
            receivedMessage = infoMessage.getText();
        }

        botAnswerUtils(infoMessage, chatId, firstName, receivedMessage);
    }

    private void botAnswerUtils(Message infoMessage, Long chatId, String firstName, String receivedMessage) {
        SendMessage answer = null;
        switch (receivedMessage) {
            case "/start":
                createUser(infoMessage);
                answer = startBot(chatId, firstName);
                break;
            case "/get":
                break;
            default:
                answer = sendMessage(chatId, DEFAULT_TEXT);
                break;
        }
        createMessage(infoMessage, userService.getUser(chatId), answer);
    }

    private void createUser(Message infoMessage) {
        userService.createUser(infoMessage);
    }

    private void createMessage(Message infoMessage, User user, SendMessage answer) {
        messageService.createMessage(infoMessage, user, answer);
    }

    private SendMessage startBot(long chatId, String firstName) {
        log.info("Replied to user: " + firstName);
        return sendMessage(chatId, (firstName + START_TEXT));
    }

    private SendMessage sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
        return sendMessage;
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void createDomain() {
        domainService.clearDomain();
        domainService.createDomain();
        mailingDomain();
    }

    private void mailingDomain() {
        var users = userService.getAllUser();
        for (User user : users) {
            sendMessage(user.getChatId(), domainService.mailingDomain());
        }
    }
}