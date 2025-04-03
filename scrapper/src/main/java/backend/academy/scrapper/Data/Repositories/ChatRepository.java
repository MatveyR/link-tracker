package backend.academy.scrapper.Data.Repositories;

import backend.academy.scrapper.Data.Models.Chat;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository extends BaseMapRepository<Chat> {
    @Override
    protected Long getId(Chat entity) {
        return entity.id();
    }
}
