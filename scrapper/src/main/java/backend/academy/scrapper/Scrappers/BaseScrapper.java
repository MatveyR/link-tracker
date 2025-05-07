package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Exceptions.NotificationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseScrapper implements Scrapper {
    protected final WebClient webClient;
    protected final BotClient botClient;
    protected final SubscriptionRepository subscriptionRepository;
    protected final LinkRepository linkRepository;

    @Override
    public void notifySubscribers(Link link) {
        List<Long> chatIds = subscriptionRepository.findUsersByLinkId(link.id());

        if (!chatIds.isEmpty()) {
            LinkUpdateRequest update = new LinkUpdateRequest(
                link.id(),
                link.linkUrl(),
                prepareUpdateMessage(link),
                chatIds
            );

            try {
                botClient.sendUpdate(update);
            } catch (WebClientResponseException e) {
                log.error("Http-ошибка при отправке обновления. Статус: {}, Тело ответа: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
                throw new NotificationException("Ошибка взаимодействия с ботом: " + e.getMessage());
            } catch (RuntimeException e) {
                log.error("Runtime-ошибка при отправке уведомления", e);
                throw new NotificationException("Runtime-ошибка при отправке уведомления");
            }
        }
    }

    protected abstract String prepareUpdateMessage(Link link);

    protected abstract boolean hasUpdates(Link link);
}
