package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Subscription;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository extends BaseMapRepository<Subscription> {
    @Override
    protected Long getId(Subscription entity) {
        return entity.id();
    }

    public List<Long> findLinksByUserId(Long userId) {
        return storage.values().stream()
                .filter(subscription -> userId.equals(subscription.chatId()))
                .map(Subscription::linkId)
                .toList();
    }

    public List<Long> findUsersByLinkId(Long linkId) {
        return storage.values().stream()
                .filter(subscription -> linkId.equals(subscription.linkId()))
                .map(Subscription::chatId)
                .toList();
    }
}
