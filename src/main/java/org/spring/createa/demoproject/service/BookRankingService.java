package org.spring.createa.demoproject.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.spring.createa.demoproject.Repository.BookRankingRepository;
import org.spring.createa.demoproject.Repository.BookRepository;
import org.spring.createa.demoproject.domain.Book;
import org.spring.createa.demoproject.domain.BookRanking;
import org.spring.createa.demoproject.dto.BookDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookRankingService {

  BookRepository bookRepository;
  Data4LibraryService data4LibraryService;
  BookRankingRepository bookRankingRepository;

  public BookRankingService(BookRepository bookRepository, Data4LibraryService data4LibraryService,
      BookRankingRepository bookRankingRepository) {
    this.bookRepository = bookRepository;
    this.data4LibraryService = data4LibraryService;
    this.bookRankingRepository = bookRankingRepository;
  }

  int DEFAULT_SIZE = 100;


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOverallRanking() {
    saveRanking(BookRanking.Category.TOTAL);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveCategoryRanking(String kdc) {
    saveRanking(BookRanking.Category.values()[Integer.parseInt(kdc)]);
  }

  @Transactional
  protected void saveRanking(BookRanking.Category category) {
    String kdc =
        category == BookRanking.Category.TOTAL ? null : Integer.toString(category.ordinal());
    Set<BookDTO> popularBooks =
        data4LibraryService.searchPopularBooksWithAPI(DEFAULT_SIZE, kdc);

    Map<String, Book> existingBooks =
        findExistingBooksByIsbn(extractIsbn13s(popularBooks));

    List<BookRanking> rankings = new ArrayList<>();
    List<Book> newBook = new ArrayList<>();

    for (BookDTO dto : popularBooks) {

      Book book = existingBooks.get(dto.isbn13());

      if (book == null) {
        book = Book.of(dto);
        newBook.add(book);
        existingBooks.put(book.getIsbn13(), book);
      }

      rankings.add(new BookRanking(book, dto.ranking(), category));
    }

    bookRepository.saveAll(newBook);
    bookRankingRepository.saveAll(rankings);
    bookRankingRepository.deleteAllByCategoryAndCreatedAtBefore(category, LocalDate.now());
  }

  private static @NonNull List<String> extractIsbn13s(Set<BookDTO> popularBooks) {
    return popularBooks.stream().map(BookDTO::isbn13).toList();
  }

  public Map<String, Book> findExistingBooksByIsbn(
      Collection<String> isbn13s) {
    List<Book> existingBooks =
        bookRepository.findByIsbn13In(isbn13s);
    return existingBooks.stream()
        .collect(Collectors.toMap(
            Book::getIsbn13,
            Function.identity()));
  }

  public List<BookDTO> getPopularBooks(int size, BookRanking.Category category) {
    return bookRankingRepository.getPopularBooks(size, category).stream()
        .map(bookRanking -> (Book.from(bookRanking.getBook(), bookRanking.getRanking())))
        .toList();
  }
}
