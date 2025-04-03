package backend.academy.scrapper.Services;

import backend.academy.scrapper.Exceptions.ResourceNotFoundException;
import backend.academy.scrapper.Data.Models.Chat;
import backend.academy.scrapper.Data.Repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public void registerChat(Long chatId) {
        if (chatId == null || chatId <= 0) {
            throw new IllegalArgumentException("Некорректный ID чата");
        }

        if (chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException("Чат уже зарегистрирован");
        }

        Chat chat = new Chat(chatId, LocalDateTime.now());
        chatRepository.save(chat);
    }

    public void deleteChat(Long chatId) {
        if (chatId == null || chatId <= 0) {
            throw new IllegalArgumentException("Некорректный ID чата");
        }

        if (!chatRepository.existsById(chatId)) {
            throw new ResourceNotFoundException("Чат не существует");
        }

        chatRepository.deleteById(chatId);
    }
}
