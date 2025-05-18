package backend.academy.scrapper.Data.Models;

import backend.academy.scrapper.Data.Utils.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="subscriptions")
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "chat_id", nullable = false)
    private final Long chatId;

    @Column(name = "link_id", nullable = false)
    private final Long linkId;

    @Column(name = "tags")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @Column(name = "filters")
    @Convert(converter = StringListConverter.class)
    private List<String> filters;
}
