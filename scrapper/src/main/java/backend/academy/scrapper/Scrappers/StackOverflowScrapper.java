package backend.academy.scrapper.Scrappers;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StackOverflowScrapper extends BaseScrapper {
    private static final String STACKOVERFLOW_API_URL = "https://api.stackexchange.com/2.3";

    public StackOverflowScrapper(WebClient.Builder webClientBuilder,
                                 BotClient botClient,
                                 SubscriptionRepository subscriptionRepository,
                                 LinkRepository linkRepository) {
        super(webClientBuilder.baseUrl(STACKOVERFLOW_API_URL).build(),
            botClient,
            subscriptionRepository,
            linkRepository);
    }

    @Override
    public void trackUpdates() {
        linkRepository.findAll().stream()
            .filter(link -> link.linkUrl().contains("stackoverflow.com"))
            .forEach(link -> {
                if (hasUpdates(link)) {
                    notifySubscribers(link);
                }
            });
    }

    @Override
    protected String prepareUpdateMessage(Link link) {
        return "Новое обновление на StackOverflow";
    }

    @Override
    protected boolean hasUpdates(Link link) {
        try {
            String response = fetchQuestionData(link);
            JsonNode jsonNode = new ObjectMapper().readTree(response);
            return checkForUpdates(link, jsonNode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private String fetchQuestionData(Link link) {
        String questionId = extractQuestionId(link.linkUrl());
        return webClient.get()
            .uri(uri -> uri.path("/questions/{id}")
                .queryParam("site", "stackoverflow")
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

    private boolean checkForUpdates(Link link, JsonNode data) {
        return data.path("items").get(0).path("answer_count").asInt() > 0;
    }
}
