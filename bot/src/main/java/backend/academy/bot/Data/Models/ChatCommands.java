package backend.academy.bot.Data.Models;

import lombok.Getter;

@Getter
public enum ChatCommands {
    START("/start", "Запуск бота"),
    HELP("/help", "Доступные команды"),
    TRACK("/track", "Начать отслеживать ресурс"),
    UNTRACK("/untrack", "Прекратить отслеживание"),
    LIST("/list", "Мои отслеживаемые ресурсы");

    private final String command;
    private final String description;

    ChatCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }
}
