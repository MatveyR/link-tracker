package backend.academy.scrapper;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Configs.ScrapperPropsConfig;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Exceptions.NotificationException;
import backend.academy.scrapper.Scrappers.BaseScrapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseScrapperTest {

    @Mock
    private WebClient webClient;

    @Mock
    private BotClient botClient;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private LinkRepository linkRepository;

    private BaseScrapper scrapper;

    @BeforeEach
    void setUp() {
        scrapper = new BaseScrapper(webClient, botClient, subscriptionRepository, linkRepository) {
            @Override
            public void trackUpdates() {

            }

            @Override
            protected String prepareUpdateMessage(Link link) {
                return "Test message";
            }

            @Override
            protected boolean hasUpdates(Link link) {
                return true;
            }
        };
    }

    @Test
    void notifySubscribers_SendsToCorrectChats() {
        // Given
        Link link = new Link(1L, "https://github.com/example");
        List<Long> chatIds = List.of(100L, 200L);

        when(subscriptionRepository.findUsersByLinkId(link.id())).thenReturn(chatIds);
        doNothing().when(botClient).sendUpdate(any());

        // When
        scrapper.notifySubscribers(link);

        // Then
        verify(botClient).sendUpdate(argThat(update ->
            update.tgChatIds().equals(chatIds) &&
                update.url().equals(link.linkUrl()) &&
                update.id().equals(link.id())
        ));
    }

    @Test
    void notifySubscribers_WhenNoSubscribers_DoesNotSend() {
        // Given
        Link link = new Link(1L, "https://github.com/example");
        when(subscriptionRepository.findUsersByLinkId(link.id())).thenReturn(List.of());

        // When
        scrapper.notifySubscribers(link);

        // Then
        verify(botClient, never()).sendUpdate(any());
    }

    @Test
    void notifySubscribers_WhenBotFails_ThrowsNotificationException() {
        // Given
        Link link = new Link(1L, "https://github.com/example");
        List<Long> chatIds = List.of(100L);

        when(subscriptionRepository.findUsersByLinkId(link.id())).thenReturn(chatIds);
        doThrow(new RuntimeException("API error")).when(botClient).sendUpdate(any());

        // When & Then
        assertThrows(NotificationException.class, () -> scrapper.notifySubscribers(link));
    }
}
