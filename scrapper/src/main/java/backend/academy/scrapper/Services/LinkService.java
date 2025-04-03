package backend.academy.scrapper.Services;

import backend.academy.scrapper.Data.DTO.Requests.AddLinkRequest;
import backend.academy.scrapper.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.scrapper.Data.DTO.Responses.LinkResponse;
import backend.academy.scrapper.Data.DTO.Responses.ListLinksResponse;
import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Models.Subscription;
import backend.academy.scrapper.Exceptions.ResourceNotFoundException;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;

    public LinkResponse addLink(Long chatId, AddLinkRequest request) {
        validateChatId(chatId);
        validateLink(request.link());

        Link link = linkRepository.findAll().stream()
            .filter(l -> l.linkUrl().equals(request.link()))
            .findFirst()
            .orElseGet(() -> {
                Link newLink = new Link(linkRepository.count(), request.link());
                return linkRepository.save(newLink);
            });

        Subscription subscription = new Subscription(
            subscriptionRepository.count(),
            chatId,
            link.id(),
            request.tags(),
            request.filters()
        );
        subscriptionRepository.save(subscription);

        return mapToLinkResponse(link, subscription);
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest request) {
        validateChatId(chatId);
        validateLink(request.link());

        Link link = linkRepository.findAll().stream()
            .filter(l -> l.linkUrl().equals(request.link()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Ссылка не найдена"));

        Subscription subscription = subscriptionRepository.findAll().stream()
            .filter(s -> s.chatId().equals(chatId) && s.linkId().equals(link.id()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Подписка не найдена"));

        subscriptionRepository.deleteById(subscription.id());

        return mapToLinkResponse(link, subscription);
    }

    public ListLinksResponse getAllLinks(Long chatId) {
        validateChatId(chatId);

        List<Long> linkIds = subscriptionRepository.findLinksByUserId(chatId);
        List<LinkResponse> links = linkIds.stream()
            .map(linkRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(link -> {
                Subscription subscription = subscriptionRepository.findAll().stream()
                    .filter(s -> s.chatId().equals(chatId) && s.linkId().equals(link.id()))
                    .findFirst()
                    .orElseThrow();
                return mapToLinkResponse(link, subscription);
            })
            .toList();

        return new ListLinksResponse(links, links.size());
    }

    private void validateChatId(Long chatId) {
        if (chatId == null || chatId <= 0) {
            throw new IllegalArgumentException("Некорректный ID чата");
        }
    }

    private void validateLink(String link) {
        if (link == null || !isValidUrl(link)) {
            throw new IllegalArgumentException("Некорректная ссылка");
        }
    }

    private boolean isValidUrl(String url) {
        try {
            URI.create(url).toURL();
            return true;
        } catch (IllegalArgumentException | MalformedURLException e) {
            return false;
        }
    }

    private LinkResponse mapToLinkResponse(Link link, Subscription subscription) {
        return new LinkResponse(
            link.id(),
            link.linkUrl(),
            subscription.tags(),
            subscription.filters()
        );
    }
}
