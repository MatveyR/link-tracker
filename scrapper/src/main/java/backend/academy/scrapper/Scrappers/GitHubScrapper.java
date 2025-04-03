package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
public class GitHubScrapper extends BaseScrapper {
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final Duration UPDATE_THRESHOLD = Duration.ofMinutes(10);

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
                try {
                    if (hasUpdates(link)) {
                        System.out.println("Обновлено " + link.linkUrl());
                        notifySubscribers(link);
                    } else {
                        System.out.println("Не обновлено " + link.linkUrl());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
    }


    @Override
    protected boolean hasUpdates(Link link) {
        String apiPath = convertToApiPath(link.linkUrl());
        Instant lastUpdated = fetchLastUpdateTime(apiPath);
        Instant tenMinutesAgo = Instant.now().minus(UPDATE_THRESHOLD);

        return lastUpdated.isAfter(tenMinutesAgo);
    }

    @Override
    protected String prepareUpdateMessage(Link link) {
        return "Обновление на GitHub";
    }

    private String convertToApiPath(String repoUrl) {
        return repoUrl.replace("https://github.com/", "/repos/")
            .replaceAll("/$", "");
    }

    private Instant fetchLastUpdateTime(String apiPath) {
        String response = webClient.get()
            .uri(apiPath)
            .header("Accept", "application/vnd.github.v3+json")
            .retrieve()
            .bodyToMono(String.class)
            .block(REQUEST_TIMEOUT);

        try {
            JsonNode repoData = new ObjectMapper().readTree(response);
            String updatedAt = repoData.path("pushed_at").asText();
            return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(updatedAt));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обработать ответ GitHub", e);
        }
    }
}
