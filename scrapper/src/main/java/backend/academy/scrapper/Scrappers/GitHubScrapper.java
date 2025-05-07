package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Configs.ScrapperPropsConfig;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class GitHubScrapper extends BaseScrapper {
    private final Duration UPDATE_THRESHOLD;
    private final Duration REQUEST_TIMEOUT;

    private final ObjectMapper objectMapper;

    public GitHubScrapper(
            WebClient.Builder webClientBuilder,
            BotClient botClient,
            SubscriptionRepository subscriptionRepository,
            LinkRepository linkRepository,
            ObjectMapper objectMapper,
            ScrapperPropsConfig propsConfig) {

        super(
                webClientBuilder.baseUrl(propsConfig.github().api_url()).build(),
                botClient,
                subscriptionRepository,
                linkRepository);

        this.objectMapper = objectMapper;
        REQUEST_TIMEOUT = Duration.ofSeconds(propsConfig.github().timeout());
        UPDATE_THRESHOLD = Duration.ofMinutes(propsConfig.github().update_threshold());
    }

    @Override
    public void trackUpdates() {
        linkRepository.findAll().stream()
                .filter(link -> link.linkUrl().contains("github.com"))
                .forEach(link -> {
                    try {
                        if (hasUpdates(link)) {
                            log.info("Обновлено: {}", link.linkUrl());
                            notifySubscribers(link);
                        } else {
                            log.info("Не обновлено: {}", link.linkUrl());
                        }
                    } catch (Exception e) {
                        log.error("Ошибка проверки обновлений гитхаб: {}", e.getMessage());
                    }
                });
    }

    @Override
    protected boolean hasUpdates(Link link) {
        String apiPath = convertToApiPath(link.linkUrl());
        Instant lastUpdated = fetchLastUpdateTime(apiPath);
        Instant lastChecked = Instant.now(Clock.system(ZoneId.systemDefault())).minus(UPDATE_THRESHOLD);

        return lastUpdated.isAfter(lastChecked);
    }

    @Override
    protected String prepareUpdateMessage(Link link) {
        return "Обновление на GitHub";
    }

    private String convertToApiPath(String repoUrl) {
        return repoUrl.replace("https://github.com/", "/repos/").replaceAll("/$", "");
    }

    @Retryable(
            retryFor = {
                WebClientResponseException.TooManyRequests.class,
                WebClientResponseException.ServiceUnavailable.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    private Instant fetchLastUpdateTime(String apiPath) {
        try {
            String response = webClient
                    .get()
                    .uri(apiPath)
                    .header("Accept", "application/vnd.github.v3+json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

            JsonNode repoData = objectMapper.readTree(response);
            String updatedAt = repoData.path("pushed_at").asText();
            return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(updatedAt));
        } catch (WebClientResponseException e) {
            log.error("Ошибка при запросе к github: {}", e.getMessage());
            throw new RuntimeException("Ошибка при запросе к GitHub API", e);
        } catch (Exception e) {
            log.error("Ошибка обработки ответа github: {}", e.getMessage());
            throw new RuntimeException("Неизвестная ошибка при обработке ответа Github", e);
        }
    }
}
