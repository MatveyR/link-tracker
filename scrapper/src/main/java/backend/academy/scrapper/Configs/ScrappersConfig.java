package backend.academy.scrapper.Configs;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Scrappers.GitHubScrapper;
import backend.academy.scrapper.Scrappers.Scrapper;
import backend.academy.scrapper.Scrappers.StackOverflowScrapper;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class ScrappersConfig {
    @Bean
    public GitHubScrapper gitHubScrapper(WebClient.Builder webClientBuilder,
                                         BotClient botClient,
                                         SubscriptionRepository subscriptionRepository,
                                         LinkRepository linkRepository) {
        return new GitHubScrapper(webClientBuilder, botClient,
            subscriptionRepository, linkRepository);
    }

    @Bean
    public StackOverflowScrapper stackOverflowScrapper(WebClient.Builder webClientBuilder,
                                                       BotClient botClient,
                                                       SubscriptionRepository subscriptionRepository,
                                                       LinkRepository linkRepository) {
        return new StackOverflowScrapper(webClientBuilder, botClient,
            subscriptionRepository, linkRepository);
    }

    @Bean
    public List<Scrapper> scrappers(List<Scrapper> scrapperList) {
        return scrapperList;
    }
}
