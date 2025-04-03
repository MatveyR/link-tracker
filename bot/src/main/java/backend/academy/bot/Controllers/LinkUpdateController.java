package backend.academy.bot.Controllers;

import backend.academy.bot.Data.DTO.Requests.LinkUpdate;
import backend.academy.bot.Services.BotUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class LinkUpdateController {
    private final BotUpdateService botUpdateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> handleUpdate(@Valid @RequestBody LinkUpdate update) {
        botUpdateService.handleIncomingUpdate(update);
        return ResponseEntity.ok().build();
    }
}
