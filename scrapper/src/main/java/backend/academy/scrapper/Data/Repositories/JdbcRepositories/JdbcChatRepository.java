package backend.academy.scrapper.Data.Repositories.JdbcRepositories;

import backend.academy.scrapper.Data.Models.Chat;
import backend.academy.scrapper.Data.Repositories.ChatRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@Profile("jdbc")
@AllArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Chat save(Chat chat) {
        return jdbcTemplate.queryForObject(
                """
            INSERT INTO chats(id, creation_date)
            VALUES (?, ?)
            ON CONFLICT (id)
            DO UPDATE SET id = EXCLUDED.id
            RETURNING id, creation_date
            """,
                (rs, rowNum) -> new Chat(rs.getLong("id"), rs.getObject("creation_date", LocalDateTime.class)),
                chat.id(),
                chat.creationDate());
    }

    @Override
    public Optional<Chat> findById(Long id) {
        try {
            Chat chat = jdbcTemplate.queryForObject(
                    "SELECT id, creation_date FROM chats WHERE id = ?",
                    (rs, rowNum) -> new Chat(rs.getLong("id"), rs.getObject("creation_date", LocalDateTime.class)),
                    id);
            return Optional.ofNullable(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Chat> findAll() {
        String sql = "SELECT id, creation_date FROM chats";
        return jdbcTemplate.query(
                sql, (rs, rowNum) -> new Chat(rs.getLong("id"), rs.getObject("creation_date", LocalDateTime.class)));
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        try {
            int affectedRows = jdbcTemplate.update("DELETE FROM chats WHERE id = ?", id);

            if (affectedRows == 0) {
                throw new EmptyResultDataAccessException(String.format("No chat found with id %d", id), 1);
            }
        } catch (Exception ex) {
            log.error("Error deleting chat with id {}", id, ex);
            throw ex;
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }

        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chats WHERE id = ?", Integer.class, id);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            log.error("Error checking existence of chat with id {}", id, e);
            return false;
        } catch (Exception e) {
            log.error("Error checking existence of chat with id {}", id, e);
            throw e;
        }
    }

    @Override
    public long count() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chats", Long.class);
            return count != null ? count : 0L;
        } catch (Exception ex) {
            log.error("Error counting chats", ex);
            throw ex;
        }
    }
}
