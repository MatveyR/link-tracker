package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Chat;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.BaseRepository;

public interface ChatRepository extends BaseRepository<Chat, Long> {
}
