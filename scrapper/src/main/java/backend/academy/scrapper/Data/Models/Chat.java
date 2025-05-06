package backend.academy.scrapper.Data.Models;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public record Chat(Long id, LocalDateTime creationDate) {
}
