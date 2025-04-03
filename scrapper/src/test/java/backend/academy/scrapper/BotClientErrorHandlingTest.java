package backend.academy.scrapper;

import backend.academy.scrapper.Clients.BotClient;
import backend.academy.scrapper.Configs.BotClientConfig;
import backend.academy.scrapper.Data.DTO.Requests.LinkUpdate;
import backend.academy.scrapper.Exceptions.NotificationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import(BotClientConfig.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class BotClientErrorHandlingTest {

    @Autowired
    private BotClient botClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    public void sendUpdate_WhenServerError_ThrowsNotificationException() {
        // Given
        LinkUpdate update = new LinkUpdate(1L, "https://example.com", "Test", List.of(123L));
        mockServer.expect(requestTo("/updates"))
            .andRespond(withServerError());

        // When & Then
        assertThrows(NotificationException.class, () -> {
            try {
                botClient.sendUpdate(update);
            } catch (ResourceAccessException e) {
                throw new NotificationException("Failed to send update");
            }
        });
    }

    @Test
    public void sendUpdate_WhenInvalidResponse_ThrowsNotificationException() {
        // Given
        LinkUpdate update = new LinkUpdate(1L, "https://example.com", "Test", List.of(123L));
        mockServer.expect(requestTo("/updates"))
            .andRespond(withSuccess("invalid", MediaType.APPLICATION_JSON));

        // When & Then
        assertThrows(ResourceAccessException.class, () -> botClient.sendUpdate(update));
    }
}
