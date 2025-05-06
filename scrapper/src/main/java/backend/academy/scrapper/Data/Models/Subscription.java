package backend.academy.scrapper.Data.Models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Subscription {
    private final Long id;

    private final Long chatId;

    private final Long linkId;

    @Setter
    private List<String> tags;

    @Setter
    private List<String> filters;
}
