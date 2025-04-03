package backend.academy.bot.Data.DTO.Responses;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {}
