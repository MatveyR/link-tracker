package backend.academy.scrapper.Data.Models;

import lombok.Getter;

@Getter
public record Link(Long id, String linkUrl) {
}
