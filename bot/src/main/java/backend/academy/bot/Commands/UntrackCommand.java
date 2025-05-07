package backend.academy.bot.Commands;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UntrackCommand implements BotCommand {
    private final LinkClient linkClient;

    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        if (args.length < 2) {
            handler.sendMessage(chatId, HumanMessages.UNTRACK_ERROR.toString());
            return;
        }

        try {
            linkClient.removeLink(chatId, new RemoveLinkRequest(args[1]));
            handler.sendMessage(chatId, HumanMessages.UNTRACK_COMPLETED.toString());
        } catch (Exception e) {
            handler.sendMessage(chatId, HumanMessages.UNTRACK_ERROR.toString());
        }
    }
}
