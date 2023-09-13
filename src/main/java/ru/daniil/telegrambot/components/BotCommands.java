package ru.daniil.telegrambot.components;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> BOT_COMMAND_LIST = List.of(
            new BotCommand("/start", "запуск бота"),
            new BotCommand("/get", "получение данных"),
            new BotCommand("/help", "информация о командах")
    );

    String START_TEXT = EmojiParser.parseToUnicode(", здравствуйте!👋\n\nЧем я могу помочь? " +
            "Чтобы узнать больше о моих возможностях, введите команду /help");

    String HELP_TEXT = EmojiParser.parseToUnicode("Что умеет этот бот:\n\n" +
            "🏁 Команда /start позволяет начать работу с ботом и зарегистрировать вас\n\n" +
            "📝 Бот автоматически собирает все ваши сообщения и сохраняет их в базе данных\n\n" +
            "✉️ Каждый день бот отправляет вам сообщение о собранных доменах");
    String DEFAULT_TEXT = "Извините, я не понимаю вашей команды!";
}
