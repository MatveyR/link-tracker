package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Configs.ScrapperPropsConfig;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Services.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class StackOverflowScrapper extends BaseScrapper {
    @Autowired
    private ScrapperPropsConfig scrapperPropsConfig;

    private static String STACKOVERFLOW_API_URL;
    private static Duration REQUEST_TIMEOUT;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StackOverflowScrapper(
        WebClient.Builder webClientBuilder,
        LinkRepository linkRepository,
        NotificationService notificationService) {
        super(
            webClientBuilder.baseUrl(STACKOVERFLOW_API_URL).build(),
            linkRepository,
            notificationService);
    }

    @PostConstruct
    private void init() {
        STACKOVERFLOW_API_URL = scrapperPropsConfig.stackOverflow().api_url();
        REQUEST_TIMEOUT = Duration.ofSeconds(scrapperPropsConfig.stackOverflow().timeout());
    }

    @Override
    public void trackUpdates() {
        linkRepository.findAll().stream()
            .filter(link -> link.linkUrl().contains("stackoverflow.com"))
            .forEach(link -> {
                try {
                    if (hasUpdates(link)) {
                        QuestionInfo questionInfo = fetchQuestionInfo(link);
                        String notificationMessage = createNotificationMessage(questionInfo);
                        log.info("Обновлено: {}", link.linkUrl());
                        notificationService.notifySubscribers(link, notificationMessage);
                    }
                } catch (Exception e) {
                    log.error("Ошибка проверки обновлений StackOverflow: {}", e.getMessage());
                }
            });
    }

    @Override
    protected boolean hasUpdates(Link link) {
        try {
            String response = fetchQuestionData(link);
            JsonNode jsonNode = objectMapper.readTree(response);
            return checkForUpdates(jsonNode);
        } catch (Exception e) {
            log.error("Ошибка проверки обновлений stackoverflow: {}", e.getMessage());
            return false;
        }
    }

    private QuestionInfo fetchQuestionInfo(Link link) throws JsonProcessingException {
        String response = fetchQuestionData(link);
        JsonNode data = objectMapper.readTree(response);
        JsonNode question = data.path("items").get(0);

        String questionId = extractQuestionId(link.linkUrl());
        String title = question.path("title").asText();
        String ownerName = question.path("owner").path("display_name").asText("Аноним");
        Instant creationDate = Instant.ofEpochSecond(question.path("creation_date").asLong());
        String preview = getPreviewContent(question);

        return new QuestionInfo(questionId, title, ownerName, creationDate, preview);
    }

    private String getPreviewContent(JsonNode question) {
        if (question.has("answers")) {
            JsonNode answers = question.path("answers");
            if (answers.size() > 0) {
                String answerBody = answers.get(0).path("body").asText();
                return truncatePreview(answerBody);
            }
        }

        if (question.has("comments")) {
            JsonNode comments = question.path("comments");
            if (comments.size() > 0) {
                String commentBody = comments.get(0).path("body").asText();
                return truncatePreview(commentBody);
            }
        }

        return truncatePreview(question.path("body").asText());
    }

    private String truncatePreview(String text) {
        if (text == null || text.isEmpty()) {
            return "(нет текста)";
        }
        String plainText = text.replaceAll("<[^>]+>", " ");
        return plainText.length() > 200
            ? plainText.substring(0, 200) + "..."
            : plainText;
    }

    private String createNotificationMessage(QuestionInfo questionInfo) {
        return String.format(
            "Новое обновление на StackOverflow!\n\n" +
                "Вопрос: %s\n" +
                "Автор: %s\n" +
                "Дата создания: %s\n" +
                "Превью: %s\n\n" +
                "Ссылка: https://stackoverflow.com/questions/%s",
            questionInfo.title(),
            questionInfo.ownerName(),
            questionInfo.creationDate().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
            questionInfo.preview(),
            questionInfo.questionId()
        );
    }

    private String fetchQuestionData(Link link) {
        String questionId = extractQuestionId(link.linkUrl());
        return webClient
            .get()
            .uri(uri -> uri.path("/questions/{id}")
                .queryParam("site", "stackoverflow")
                .queryParam("filter", "withbody")
                .build(questionId))
            .retrieve()
            .bodyToMono(String.class)
            .block(REQUEST_TIMEOUT);
    }

    private String extractQuestionId(String url) {
        Matcher matcher = Pattern.compile("stackoverflow.com/questions/(\\d+)").matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Неверная ссылка на вопрос StackOverflow: " + url);
    }

    private boolean checkForUpdates(JsonNode data) {
        JsonNode question = data.path("items").get(0);
        return question.path("answer_count").asInt() > 0 ||
            question.path("comment_count").asInt() > 0 ||
            question.path("last_activity_date").asLong() > question.path("creation_date").asLong();
    }

    private record QuestionInfo(
        String questionId,
        String title,
        String ownerName,
        Instant creationDate,
        String preview
    ) {
    }
}
