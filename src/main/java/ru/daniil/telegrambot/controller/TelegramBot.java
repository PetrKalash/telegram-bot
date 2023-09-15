package ru.daniil.telegrambot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ru.daniil.telegrambot.service.MessageService;
import ru.daniil.telegrambot.service.UserService;

@Slf4j
@Component
@Service
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    private final BotConfig botConfig;
    private final UserService userService;
    private final MessageService messageService;
    private final SendMessageComponent sendMessageComponent;

    @Autowired
    public TelegramBot(
            @Value("${bot.token}") String botToken,
            BotConfig botConfig,
            UserService userService,
            MessageService messageService,
            SendMessageComponent sendMessageComponent)
    {
        super(botToken);
        this.botConfig = botConfig;
        this.userService = userService;
        this.messageService = messageService;
        this.sendMessageComponent = sendMessageComponent;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        botMenu();
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            long chatId = message.getChatId();
            String firstName = message.getChat().getFirstName();
            String receivedMessage = message.getText();

            botAnswerUtils(message, chatId, firstName, receivedMessage);
        }
    }

    private void botMenu() {
        try {
            this.execute(
                    new SetMyCommands(BOT_COMMAND_LIST, new BotCommandScopeDefault(), null)
            );
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    private void botAnswerUtils(
            Message message,
            Long chatId,
            String firstName,
            String receivedMessage)
    {
        String text;
        switch (receivedMessage) {
            case "/start":
                userService.createUser(message);
                text = firstName + START_TEXT;
                break;
            case "/help":
                text = HELP_TEXT;
                break;
            default:
                text = DEFAULT_TEXT;
                break;
        }
        sendMessage(text, chatId);
        createMessage(message, chatId);
    }

    public void sendMessage(String message, long chatId) {
        sendMessageComponent.createSendMessage(chatId, message);
        try {
            execute(sendMessageComponent.getSendMessage());
            log.info("Replied message: " + message);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    private void createMessage(Message message, long chatId) {
        User user = userService.getUser(chatId);
        SendMessage answer = sendMessageComponent.getSendMessage();
        messageService.createMessage(message, user, answer);
    }
}