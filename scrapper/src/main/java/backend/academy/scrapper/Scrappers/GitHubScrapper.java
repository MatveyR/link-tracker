package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GitHubScrapper extends BaseScrapper {
    private static final String GITHUB_API_URL = "https://api.github.com";

    public GitHubScrapper(WebClient.Builder webClientBuilder,
                          BotClient botClient,
                          SubscriptionRepository subscriptionRepository,
                          LinkRepository linkRepository) {
        super(webClientBuilder.baseUrl(GITHUB_API_URL).build(),
            botClient,
            subscriptionRepository,
            linkRepository);
    }

    @Override
    public void trackUpdates() {
        linkRepository.findAll().stream()
            .filter(link -> link.linkUrl().contains("github.com"))
            .forEach(link -> {
                if (hasUpdates(link)) {
                    notifySubscribers(link);
                }
            });
    }

    @Override
    protected boolean hasUpdates(Link link) {
        try {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String prepareUpdateMessage(Link link) {
        return "Обновление на GitHub";
    }

}
