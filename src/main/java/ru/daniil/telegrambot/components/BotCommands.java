package ru.daniil.telegrambot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> BOT_COMMAND_LIST = List.of(
            new BotCommand("/start", "запуск бота"),
            new BotCommand("/get", "получение данных"),
            new BotCommand("/help", "информация о командах")
    );

    String START_TEXT = ", здравствуйте! Чем я могу помочь? " +
            "Чтобы узнать больше о моих возможностях, введите команду /help";
    String DEFAULT_TEXT = "Извините, я не понимаю вашей команды!";
}
