package backend.academy.bot.Data.DTO.Responses;

import java.util.List;

public record LinkResponse(
    Long id,
    String url,
    List<String> tags,
    List<String> filters
) {
}
