package backend.academy.bot.Clients;

import backend.academy.bot.Data.DTO.Requests.AddLinkRequest;
import backend.academy.bot.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.bot.Data.DTO.Responses.LinkResponse;
import backend.academy.bot.Data.DTO.Responses.ListLinksResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/links")
public interface LinkClient {
    @GetExchange
    ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long chatId);

    @PostExchange
    LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody AddLinkRequest request
    );

    @DeleteExchange
    LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody RemoveLinkRequest request
    );
}
