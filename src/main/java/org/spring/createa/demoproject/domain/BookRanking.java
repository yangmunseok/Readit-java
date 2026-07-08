package org.spring.createa.demoproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookRanking {

  public static enum Category {
    GENERAL_WORK,
    PHILOSOPHY,
    RELIGION,
    SOCIAL_SCIENCE,
    NATURAL_SCIENCE,
    TECHNOLOGY,
    ART,
    LANGUAGE,
    LITERATURE,
    HISTORY,
    TOTAL
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Getter
  @ManyToOne
  Book book;

  @Getter
  int ranking;

  Category category;

  @CreatedDate
  LocalDate createdAt;

  public BookRanking(Book book, int ranking, Category category) {
    this.book = book;
    this.ranking = ranking;
    this.category = category;
  }

  public BookRanking() {

  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BookRanking bookRanking)) {
      return false;
    }
    return Objects.equals(book, bookRanking.book) && Objects.equals(createdAt,
        bookRanking.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(book, createdAt);
  }
}
