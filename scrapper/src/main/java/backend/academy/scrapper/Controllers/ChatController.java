package backend.academy.scrapper.Controllers;

import backend.academy.scrapper.Services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        chatService.registerChat(id);
        return ResponseEntity.ok("Чат зарегистрирован");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.ok("Чат успешно удалён");
    }
}
