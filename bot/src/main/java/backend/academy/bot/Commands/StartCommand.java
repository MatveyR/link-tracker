package backend.academy.bot.Commands;

import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartCommand implements BotCommand {
    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        handler.sendMessage(chatId, HumanMessages.HELLO.toString());
    }
}
