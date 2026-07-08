package org.spring.createa.demoproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.spring.createa.demoproject.dto.BookDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(unique = true)
  String isbn13;

  String vol;

  String bookName;
  String author;
  String bookImageUrl;
  String classNo;
  String publisher;
  String publicationYear;

  @CreatedDate
  LocalDateTime createdAt;
  @UpdateTimestamp
  LocalDateTime updatedAt;

  public static Book of(BookDTO bookDTO) {
    return Book.builder()
        .isbn13(bookDTO.isbn13())
        .vol(bookDTO.vol())
        .bookName(bookDTO.bookname())
        .author(bookDTO.authors())
        .bookImageUrl(bookDTO.bookImageURL())
        .publisher(bookDTO.publisher())
        .publicationYear(bookDTO.publication_year())
        .build();
  }

  public static BookDTO from(Book book, Integer ranking) {
    return new BookDTO(null, book.getBookName(), book.getAuthor(), book.getPublisher(), null,
        book.getPublicationYear(), book.getIsbn13(), null, book.getVol(), book.getClassNo(), null,
        null, book.getBookImageUrl(), ranking);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Book book)) {
      return false;
    }
    if (Objects.equals(isbn13, book.isbn13)) {
      System.out.println(isbn13);
    }
    return Objects.equals(isbn13, book.isbn13);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode("book" + isbn13);
  }
}
