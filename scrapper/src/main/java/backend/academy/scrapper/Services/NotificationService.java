package backend.academy.scrapper.Services;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Exceptions.NotificationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final BotClient botClient;
    private final SubscriptionRepository subscriptionRepository;

    public void notifySubscribers(Link link, String updateMessage) {
        List<Long> chatIds = subscriptionRepository.findUsersByLinkId(link.id());

        if (!chatIds.isEmpty()) {
            LinkUpdateRequest update =
                new LinkUpdateRequest(link.id(), link.linkUrl(), updateMessage, chatIds);

            try {
                botClient.sendUpdate(update);
            } catch (WebClientResponseException e) {
                log.error(
                    "Http-ошибка при отправке обновления. Статус: {}, Тело ответа: {}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
                throw new NotificationException("Ошибка взаимодействия с ботом: " + e.getMessage());
            } catch (RuntimeException e) {
                log.error("Runtime-ошибка при отправке уведомления", e);
                throw new NotificationException("Runtime-ошибка при отправке уведомления");
            }
        }
    }
}
