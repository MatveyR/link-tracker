package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Configs.ScrapperPropsConfig;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Services.NotificationService;
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
        LinkRepository linkRepository,
        ObjectMapper objectMapper,
        ScrapperPropsConfig propsConfig,
        NotificationService notificationService) {

        super(
            webClientBuilder.baseUrl(propsConfig.github().api_url()).build(),
            linkRepository,
            notificationService);

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
                        String apiPath = convertToApiPath(link.linkUrl());
                        RepositoryInfo repoInfo = fetchRepositoryInfo(apiPath);
                        String notificationMessage = createNotificationMessage(repoInfo);
                        log.info("Обновлено: {}", link.linkUrl());
                        notificationService.notifySubscribers(link, notificationMessage);
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

    private String convertToApiPath(String repoUrl) {
        return repoUrl.replace("https://github.com/", "/repos/").replaceAll("/$", "");
    }

    private String createNotificationMessage(RepositoryInfo repoInfo) {
        return String.format(
            "Репозиторий обновлен!\n\n" +
                "Название: %s\n" +
                "Владелец: %s\n" +
                "Последнее обновление: %s\n" +
                "Описание: %s\n\n" +
                "Ссылка: https://github.com/%s/%s",
            repoInfo.name(),
            repoInfo.ownerLogin(),
            repoInfo.pushedAt().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
            repoInfo.description(),
            repoInfo.ownerLogin(),
            repoInfo.name()
        );
    }

    @Retryable(
        retryFor = {
            WebClientResponseException.TooManyRequests.class,
            WebClientResponseException.ServiceUnavailable.class
        },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2))
    private RepositoryInfo fetchRepositoryInfo(String apiPath) {
        try {
            String response = webClient
                .get()
                .uri(apiPath)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .bodyToMono(String.class)
                .block(REQUEST_TIMEOUT);

            JsonNode repoData = objectMapper.readTree(response);

            String name = repoData.path("name").asText();
            String ownerLogin = repoData.path("owner").path("login").asText();
            String description = repoData.path("description").asText("(нет описания)");
            if (description.length() > 200) {
                description = description.substring(0, 200) + "...";
            }
            Instant pushedAt = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(repoData.path("pushed_at").asText()));

            return new RepositoryInfo(name, ownerLogin, description, pushedAt);
        } catch (WebClientResponseException e) {
            log.error("Ошибка при запросе к github: {}", e.getMessage());
            throw new RuntimeException("Ошибка при запросе к GitHub API", e);
        } catch (Exception e) {
            log.error("Ошибка обработки ответа github: {}", e.getMessage());
            throw new RuntimeException("Неизвестная ошибка при обработке ответа Github", e);
        }
    }

    private Instant fetchLastUpdateTime(String apiPath) {
        RepositoryInfo repoInfo = fetchRepositoryInfo(apiPath);
        return repoInfo.pushedAt();
    }

    private record RepositoryInfo(
        String name,
        String ownerLogin,
        String description,
        Instant pushedAt
    ) {
    }
}
