package org.spring.createa.demoproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
    @Index(name = "rank_created_at_category_ranking_idx", columnList = "created_at, category, ranking")})
public class BookRanking {

  @Getter
  public static enum Category {
    TOTAL("전체"),
    GENERAL_WORK("총류"),
    PHILOSOPHY("철학"),
    RELIGION("종교"),
    SOCIAL_SCIENCE("사회과학"),
    NATURAL_SCIENCE("자연과학"),
    TECHNOLOGY("기술과학"),
    ART("예술"),
    LANGUAGE("언어"),
    LITERATURE("문학"),
    HISTORY("역사");

    private final String value;

    Category(String value) {
      this.value = value;
    }

    public static Category from(String value) {
      return Arrays.stream(values())
          .filter(c -> c.value.equals(value))
          .findFirst()
          .orElseThrow();
    }

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
