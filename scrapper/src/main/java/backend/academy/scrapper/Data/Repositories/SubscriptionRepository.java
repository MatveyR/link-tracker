package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Subscription;
import backend.academy.scrapper.Data.Repositories.JdbcRepositories.BaseRepository;
import java.util.List;

public interface SubscriptionRepository extends BaseRepository<Subscription, Long> {
    List<Long> findLinksByUserId(Long userId);

    List<Long> findUsersByLinkId(Long linkId);
}
