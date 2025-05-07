package backend.academy.bot.Data.DTO.Requests;

import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

public record AddLinkRequest(
    @NotBlank String link,
    Collection<String> tags,
    Collection<String> filters
) {}
