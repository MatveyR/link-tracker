package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Exceptions.NotificationException;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public abstract class BaseScrapper implements Scrapper {
    protected final WebClient webClient;
    protected final BotClient botClient;
    protected final SubscriptionRepository subscriptionRepository;
    protected final LinkRepository linkRepository;

    protected static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

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
            } catch (Exception e) {
                throw new NotificationException("Ошибка при отправке запроса боту");
            }
        }
    }

    protected abstract String prepareUpdateMessage(Link link);

    protected abstract boolean hasUpdates(Link link);
}
