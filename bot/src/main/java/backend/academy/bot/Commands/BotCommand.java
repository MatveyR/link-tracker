package backend.academy.bot.Commands;

import backend.academy.bot.Handlers.BotUpdateHandler;

public interface BotCommand {
    void execute(Long chatId, String[] args, BotUpdateHandler handler);
}
