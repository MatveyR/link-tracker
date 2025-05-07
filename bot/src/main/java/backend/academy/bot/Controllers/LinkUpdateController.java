package backend.academy.bot.Controllers;

import backend.academy.bot.Data.DTO.Requests.LinkUpdateRequest;
import backend.academy.bot.Services.BotUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class LinkUpdateController {
    private final BotUpdateService botUpdateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> handleUpdate(@RequestBody LinkUpdateRequest update) {
        log.info("Пришло обновление в чат {}", update.tgChatIds());
        botUpdateService.handleIncomingUpdate(update);
        return ResponseEntity.ok().build();
    }
}
