package backend.academy.scrapper.Data.Repositories.JdbcRepositories;

import backend.academy.scrapper.Data.Models.Subscription;
import backend.academy.scrapper.Data.Repositories.SubscriptionRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
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
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findLinksByUserId(Long userId) {
        String sql = "SELECT link_id FROM subscriptions WHERE chat_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public List<Long> findUsersByLinkId(Long linkId) {
        String sql = "SELECT chat_id FROM subscriptions WHERE link_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, linkId);
    }

    @Override
    public Subscription save(Subscription subscription) {
        if (subscription.id() == null) {
            return insert(subscription);
        } else {
            return update(subscription);
        }
    }

    private Subscription insert(Subscription subscription) {
        String sql =
                """
        INSERT INTO subscriptions(chat_id, link_id, tags, filters)
        VALUES (?, ?, ?, ?)
        RETURNING id, chat_id, link_id, tags, filters
        """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Subscription(
                        rs.getLong("id"),
                        rs.getLong("chat_id"),
                        rs.getLong("link_id"),
                        rs.getString("tags") != null
                                ? Arrays.asList(rs.getString("tags").split(","))
                                : null,
                        rs.getString("filters") != null
                                ? Arrays.asList(rs.getString("filters").split(","))
                                : null),
                subscription.chatId(),
                subscription.linkId(),
                subscription.tags() != null ? String.join(",", subscription.tags()) : null,
                subscription.filters() != null ? String.join(",", subscription.filters()) : null);
    }

    private Subscription update(Subscription subscription) {
        String sql =
                """
            UPDATE subscriptions
            SET chat_id = ?, link_id = ?, tags = ?, filters = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(
                sql,
                subscription.chatId(),
                subscription.linkId(),
                subscription.tags() != null ? String.join(",", subscription.tags()) : null,
                subscription.filters() != null ? String.join(",", subscription.filters()) : null,
                subscription.id());

        return subscription;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        String sql =
                """
            SELECT id, chat_id, link_id, tags, filters
            FROM subscriptions
            WHERE id = ?
            """;

        try {
            Subscription subscription = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapSubscription(rs), id);
            return Optional.ofNullable(subscription);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Subscription> findAll() {
        String sql = "SELECT id, chat_id, link_id, tags, filters FROM subscriptions";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapSubscription(rs));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM subscriptions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) > 0 FROM subscriptions WHERE id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public long count() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM subscriptions", Long.class);
            return count != null ? count : 0L;
        } catch (Exception ex) {
            log.error("Error counting subscriptions", ex);
            throw ex;
        }
    }

    private Subscription mapSubscription(ResultSet rs) throws SQLException {
        List<String> tags = rs.getString("tags") != null
                ? Arrays.asList(rs.getString("tags").split(","))
                : null;

        List<String> filters = rs.getString("filters") != null
                ? Arrays.asList(rs.getString("filters").split(","))
                : null;

        return new Subscription(rs.getLong("id"), rs.getLong("chat_id"), rs.getLong("link_id"), tags, filters);
    }
}
