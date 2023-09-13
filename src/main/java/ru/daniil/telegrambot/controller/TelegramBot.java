package ru.daniil.telegrambot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ru.daniil.telegrambot.components.SendMessageComponent;
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
    private final SendMessageComponent sendMessageComponent;

    @Autowired
    public TelegramBot(
            @Value("${bot.token}") String botToken,
            BotConfig botConfig,
            UserService userService,
            MessageService messageService,
            DomainService domainService,
            SendMessageComponent sendMessageComponent)
    {
        super(botToken);
        this.botConfig = botConfig;
        this.userService = userService;
        this.messageService = messageService;
        this.domainService = domainService;
        this.sendMessageComponent = sendMessageComponent;
        try {
            this.execute(
                    new SetMyCommands(BOT_COMMAND_LIST, new BotCommandScopeDefault(), null)
            );
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = 0;
        String firstName = "";
        String receivedMessage = "";

        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            chatId = message.getChatId();
            firstName = message.getChat().getFirstName();
            receivedMessage = message.getText();
        }

        botAnswerUtils(message, chatId, firstName, receivedMessage);
    }

    private void botAnswerUtils(
            Message message,
            Long chatId,
            String firstName,
            String receivedMessage)
    {
        switch (receivedMessage) {
            case "/start":
                createUser(message);
                sendMessage(chatId, (firstName + START_TEXT));
                break;
            case "/help":
                sendMessage(chatId, HELP_TEXT);
                break;
            default:
                sendMessage(chatId, DEFAULT_TEXT);
                break;
        }
        createMessage(message);
    }

    private void createUser(Message message) {
        userService.createUser(message);
    }

    private void sendMessage(long chatId, String message) {
        sendMessageComponent.saveMessage(chatId, message);
        try {
            execute(sendMessageComponent.getSendMessage());
            log.info("Replied message: " + message);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    private void createMessage(Message message) {
        User user = userService.getUser(message.getChatId());
        SendMessage answer = sendMessageComponent.getSendMessage();
        messageService.createMessage(message, user, answer);
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void createDomain() {
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