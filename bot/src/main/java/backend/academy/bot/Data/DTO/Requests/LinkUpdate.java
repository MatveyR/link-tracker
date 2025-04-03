package backend.academy.bot.Data.DTO.Requests;

import java.util.List;

public record LinkUpdate(
    Long id,
    String url,
    String description,
    List<Long> tgChatIds
) {

}
