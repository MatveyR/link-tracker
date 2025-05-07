package backend.academy.bot.Commands;

import backend.academy.bot.Data.Models.UserFSM;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements BotCommand {
    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        if (args.length < 2) {
            handler.sendMessage(chatId, HumanMessages.TRACK_LINK_ERROR.toString());
            return;
        }

        UserFSM fsm = UserFSM.builder()
            .chatId(chatId)
            .build();

        handler.userFSMs().put(chatId, fsm);
        handler.handleFsmState(chatId, args);
    }
}
