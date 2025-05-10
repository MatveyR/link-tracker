package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.BaseRepository;

public interface LinkRepository extends BaseRepository<Link, Long> {
    Link findByUrl(String url);
}
