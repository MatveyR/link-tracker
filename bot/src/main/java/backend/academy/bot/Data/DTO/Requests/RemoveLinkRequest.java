package backend.academy.bot.Data.DTO.Requests;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(@NotBlank String link) {}
