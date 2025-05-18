package backend.academy.scrapper.Data.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public record Chat(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        @Column(name = "creation_date", nullable = false) LocalDateTime creationDate) {}
