package backend.academy.bot.Clients;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/tg-chat")
public interface ChatClient {

    @PostExchange("/{id}")
    ResponseEntity<String> registerChat(@PathVariable Long id);

    @DeleteExchange("/{id}")
    ResponseEntity<String> deleteChat(@PathVariable Long id);
}
