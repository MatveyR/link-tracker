package backend.academy.scrapper.Controllers;

import backend.academy.scrapper.Data.DTO.Requests.AddLinkRequest;
import backend.academy.scrapper.Data.DTO.Requests.RemoveLinkRequest;
import backend.academy.scrapper.Data.DTO.Responses.LinkResponse;
import backend.academy.scrapper.Data.DTO.Responses.ListLinksResponse;
import backend.academy.scrapper.Services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return ResponseEntity.ok(linkService.getAllLinks(chatId));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody AddLinkRequest request
    ) {
        return ResponseEntity.ok(linkService.addLink(chatId, request));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody RemoveLinkRequest request
    ) {
        return ResponseEntity.ok(linkService.removeLink(chatId, request));
    }
}
