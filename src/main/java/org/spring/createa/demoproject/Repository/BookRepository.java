package org.spring.createa.demoproject.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.spring.createa.demoproject.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer> {

  @Query("select b.isbn13 from Book b where b.isbn13 in :isbn13List")
  Set<String> findExistingIsbn13s(@Param("isbn13List") List<String> isbn13List);

  List<Book> findByIsbn13In(Collection<String> isbn13s);

  Page<Book> findBooksByCreatedAtAfterOrUpdatedAtAfter(LocalDateTime createdAtAfter,
      LocalDateTime updatedAtAfter, Pageable pageable);
}
