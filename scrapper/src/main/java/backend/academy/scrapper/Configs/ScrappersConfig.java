package backend.academy.scrapper.Configs;

import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Scrappers.GitHubScrapper;
import backend.academy.scrapper.Scrappers.Scrapper;
import backend.academy.scrapper.Scrappers.StackOverflowScrapper;
import backend.academy.scrapper.Services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class ScrappersConfig {
    @Bean
    public GitHubScrapper gitHubScrapper(
        WebClient.Builder webClientBuilder,
        LinkRepository linkRepository,
        ObjectMapper objectMapper,
        ScrapperPropsConfig scrapperPropsConfig,
        NotificationService notificationService) {
        return new GitHubScrapper(
            webClientBuilder, linkRepository, objectMapper, scrapperPropsConfig, notificationService);
    }

    @Bean
    public StackOverflowScrapper stackOverflowScrapper(
        WebClient.Builder webClientBuilder,
        LinkRepository linkRepository,
        NotificationService notificationService) {
        return new StackOverflowScrapper(webClientBuilder, linkRepository, notificationService);
    }

    @Bean
    public List<Scrapper> scrappers(List<Scrapper> scrapperList) {
        return scrapperList;
    }
}
