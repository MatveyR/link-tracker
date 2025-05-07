package backend.academy.scrapper.Data.DTO.Requests;

import java.util.List;

public record LinkUpdateRequest(Long id, String url, String description, List<Long> tgChatIds) {}
