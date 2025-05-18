package backend.academy.scrapper.Data.Repositories.JdbcRepositories;

import backend.academy.scrapper.Data.Models.Link;
import backend.academy.scrapper.Data.Repositories.LinkRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Slf4j
@Profile("jdbc")
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Link findByUrl(String url) {
        String sql = "SELECT id, url FROM links WHERE url = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapLink(rs), url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Link save(Link link) {
        if (link.id() == null) {
            return insert(link);
        } else {
            return update(link);
        }
    }

    private Link insert(Link link) {
        String sql = "INSERT INTO links (url) VALUES (?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, link.linkUrl());
        return new Link(id, link.linkUrl());
    }

    private Link update(Link link) {
        String sql = "UPDATE links SET url = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, link.linkUrl(), link.id());
        return updated > 0 ? link : null;
    }

    @Override
    public Optional<Link> findById(Long id) {
        String sql = "SELECT id, url FROM links WHERE id = ?";
        try {
            Link link = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapLink(rs), id);
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Link> findAll() {
        String sql = "SELECT id, url FROM links";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapLink(rs));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM links WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM links";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private Link mapLink(ResultSet rs) throws SQLException {
        return new Link(rs.getLong("id"), rs.getString("url"));
    }
}
