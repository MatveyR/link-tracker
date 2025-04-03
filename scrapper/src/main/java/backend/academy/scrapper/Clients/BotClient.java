package backend.academy.scrapper.Clients;

import backend.academy.scrapper.Data.DTO.Requests.LinkUpdate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/updates", accept = "application/json")
public interface BotClient {
    @PostExchange
    void sendUpdate(@RequestBody LinkUpdate linkUpdate);
}
