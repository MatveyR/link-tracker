package backend.academy.scrapper.Services;

import backend.academy.scrapper.Data.Models.Chat;
import backend.academy.scrapper.Data.Repositories.ChatRepository;
import backend.academy.scrapper.Exceptions.AlreadyExistsException;
import backend.academy.scrapper.Exceptions.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public void registerChat(Long chatId) {
        if (chatId == null || chatId <= 0) {
            throw new IllegalArgumentException("Некорректный ID чата");
        }

        if (chatRepository.existsById(chatId)) {
            throw new AlreadyExistsException("Чат уже зарегистрирован");
        }

        Chat chat = new Chat(chatId, LocalDateTime.now(ZoneId.systemDefault()));
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
