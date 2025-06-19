package backend.academy.scrapper;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Services.NotificationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private BotClient botClient;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void notifySubscribers_shouldSendCorrectUpdateRequestForGitHub() {
        // Arrange
        Link link = new Link(1L, "https://github.com/owner/repo");
        String updateMessage = "Репозиторий обновлен!\n\nНазвание: repo\nВладелец: owner\n...";
        List<Long> chatIds = List.of(123L, 456L);

        when(subscriptionRepository.findUsersByLinkId(1L)).thenReturn(chatIds);

        // Act
        notificationService.notifySubscribers(link, updateMessage);

        // Assert
        verify(subscriptionRepository).findUsersByLinkId(1L);

        LinkUpdateRequest expectedRequest = new LinkUpdateRequest(
            1L,
            "https://github.com/owner/repo",
            updateMessage,
            chatIds
        );

        verify(botClient).sendUpdate(expectedRequest);
    }

    @Test
    void notifySubscribers_shouldSendCorrectUpdateRequestForStackOverflow() {
        // Arrange
        Link link = new Link(2L, "https://stackoverflow.com/questions/123");
        String updateMessage = "Новое обновление на StackOverflow!\n\nВопрос: Как сделать...";
        List<Long> chatIds = List.of(789L);

        when(subscriptionRepository.findUsersByLinkId(2L)).thenReturn(chatIds);

        // Act
        notificationService.notifySubscribers(link, updateMessage);

        // Assert
        verify(subscriptionRepository).findUsersByLinkId(2L);

        LinkUpdateRequest expectedRequest = new LinkUpdateRequest(
            2L,
            "https://stackoverflow.com/questions/123",
            updateMessage,
            chatIds
        );

        verify(botClient).sendUpdate(expectedRequest);
    }

    @Test
    void notifySubscribers_shouldHandleEmptySubscribers() {
        // Arrange
        Link link = new Link(3L, "https://example.com");
        when(subscriptionRepository.findUsersByLinkId(3L)).thenReturn(List.of());

        // Act
        notificationService.notifySubscribers(link, "Some message");

        // Assert
        verify(subscriptionRepository).findUsersByLinkId(3L);
        verifyNoInteractions(botClient);
    }

    @Test
    void notifySubscribers_shouldFormatMessageCorrectly() {
        // Arrange
        Link link = new Link(4L, "https://github.com/test/repo");
        String updateMessage = """
            Репозиторий обновлен!

            Название: repo
            Владелец: test
            Последнее обновление: 01.01.2023 12:00
            Описание: Совы не то чем кажутся...

            Ссылка: https://github.com/test/repo""";

        List<Long> chatIds = List.of(111L);
        when(subscriptionRepository.findUsersByLinkId(4L)).thenReturn(chatIds);

        // Act
        notificationService.notifySubscribers(link, updateMessage);

        // Assert
        LinkUpdateRequest expectedRequest = new LinkUpdateRequest(
            4L,
            "https://github.com/test/repo",
            updateMessage,
            chatIds
        );

        verify(botClient).sendUpdate(expectedRequest);
    }
}
