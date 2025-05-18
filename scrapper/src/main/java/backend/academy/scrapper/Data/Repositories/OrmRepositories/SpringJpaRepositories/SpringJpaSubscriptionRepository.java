package backend.academy.scrapper.Data.Repositories.OrmRepositories.SpringJpaRepositories;

import backend.academy.scrapper.Data.Models.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaSubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByChatId(Long chatId);

    List<Subscription> findByLinkId(Long linkId);

    default List<Long> findLinksByUserId(Long userId) {
        return findByChatId(userId).stream().map(Subscription::linkId).toList();
    }

    default List<Long> findUsersByLinkId(Long linkId) {
        return findByLinkId(linkId).stream().map(Subscription::chatId).toList();
    }
}
