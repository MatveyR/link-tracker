package backend.academy.bot.Commands;

import backend.academy.bot.Handlers.BotUpdateHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCommand implements BotCommand {
    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        handler.sendError(chatId);
    }
}
