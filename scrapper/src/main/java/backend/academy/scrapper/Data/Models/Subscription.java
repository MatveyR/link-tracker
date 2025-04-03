package backend.academy.scrapper.Data.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@Getter
public class Subscription {
    Long id;

    Long chatId;

    Long linkId;

    @Setter
    List<String> tags;

    @Setter
    List<String> filters;
}
