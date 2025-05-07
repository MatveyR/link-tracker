package backend.academy.bot.Commands;

import backend.academy.bot.Clients.LinkClient;
import backend.academy.bot.Data.DTO.Responses.ListLinksResponse;
import backend.academy.bot.Handlers.BotUpdateHandler;
import backend.academy.bot.HumanMessages.HumanMessages;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCommand implements BotCommand {
    private final LinkClient linkClient;

    @Override
    public void execute(Long chatId, String[] args, BotUpdateHandler handler) {
        try {
            ListLinksResponse response = linkClient.getAllLinks(chatId);
            String message = handler.formatLinksList(response);
            handler.sendMessage(chatId, message);
        } catch (Exception e) {
            handler.sendMessage(chatId, HumanMessages.LIST_ERROR.toString());
        }
    }
}
