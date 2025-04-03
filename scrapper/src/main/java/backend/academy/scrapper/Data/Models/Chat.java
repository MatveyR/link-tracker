package backend.academy.scrapper.Data.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Chat {
    Long id;

    LocalDateTime creationDate;
}
