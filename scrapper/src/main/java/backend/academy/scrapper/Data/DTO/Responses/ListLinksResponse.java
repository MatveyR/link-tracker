package backend.academy.scrapper.Data.DTO.Responses;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}
