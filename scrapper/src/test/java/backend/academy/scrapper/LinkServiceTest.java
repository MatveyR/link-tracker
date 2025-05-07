package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import backend.academy.scrapper.Data.DTO.Requests.AddLinkRequest;
import backend.academy.scrapper.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.scrapper.Data.DTO.Responses.LinkResponse;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Models.Subscription;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import backend.academy.scrapper.Services.LinkService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private LinkService linkService;

    private final Long testChatId = 1L;
    private final String testUrl = "https://github.com/example";

    @Test
    void addLink_SavesCorrectData() {
        AddLinkRequest request = new AddLinkRequest(testUrl, List.of("tag1", "tag2"), List.of("filter1"));

        when(linkRepository.findAll()).thenReturn(List.of());
        when(linkRepository.save(any(Link.class))).thenAnswer(inv -> {
            Link l = inv.getArgument(0);
            return new Link(1L, l.linkUrl());
        });
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> {
            Subscription s = inv.getArgument(0);
            return new Subscription(1L, s.chatId(), s.linkId(), s.tags(), s.filters());
        });

        LinkResponse response = linkService.addLink(testChatId, request);

        assertAll(
                () -> assertEquals(testUrl, response.url()),
                () -> assertEquals(request.tags(), response.tags()),
                () -> assertEquals(request.filters(), response.filters()),
                () -> verify(linkRepository).save(argThat(l -> l.linkUrl().equals(testUrl))),
                () -> verify(subscriptionRepository)
                        .save(argThat(s -> s.chatId().equals(testChatId)
                                && s.tags().equals(request.tags())
                                && s.filters().equals(request.filters()))));
    }

    @Test
    void removeLink_WhenExists_RemovesSuccessfully() {
        Link existingLink = new Link(1L, testUrl);
        Subscription subscription = new Subscription(1L, testChatId, 1L, List.of(), List.of());

        when(linkRepository.findAll()).thenReturn(List.of(existingLink));
        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));

        LinkResponse response = linkService.removeLink(testChatId, new RemoveLinkRequest(testUrl));

        verify(subscriptionRepository).deleteById(subscription.id());
        assertEquals(existingLink.id(), response.id());
    }
}
