package backend.academy.bot.Commands;

import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelpCommand implements BotCommand {
    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        handler.sendMessage(chatId, HumanMessages.HELP.toString());
    }
}
