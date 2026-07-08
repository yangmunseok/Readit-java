package org.spring.createa.demoproject.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.spring.createa.demoproject.Repository.BookRepository;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.dto.response.GetBookRecommendationResponse;
import org.spring.createa.demoproject.dto.response.SearchBooksResponse;
import org.spring.createa.demoproject.dto.response.SearchDetailResponse;
import org.spring.createa.demoproject.dto.response.SearchLibrariesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Data4LibraryService {

  Data4LibraryServiceApi data4LibraryServiceApi;
  BookRepository bookRepository;

  @Value("${api.data4library.key}")
  String authKey;
  String format = "json";

  public Data4LibraryService(Data4LibraryServiceApi data4LibraryServiceApi,
      BookRepository bookRepository) {
    this.data4LibraryServiceApi = data4LibraryServiceApi;
    this.bookRepository = bookRepository;
  }

  public SearchDetailResponse getBookByIsbn(String isbn13) {
    return data4LibraryServiceApi.getBookByIsbn(isbn13, authKey, format);
  }

  @NonNull
  public Set<BookDTO> searchPopularBooksWithAPI(Integer size, String kdc) {
    Set<BookDTO> books = data4LibraryServiceApi
        .getPopularBooks(size, kdc, authKey, format)
        .response()
        .docs()
        .stream()
        .map(doc -> {
          return doc.doc().toBook();
        })
        .collect(Collectors.toSet());
    return books;
  }

  @Deprecated
  public SearchBooksResponse searchBooks(String isbn13,
      List<String> keyword,
      String publisher,
      Integer pageNo,
      Integer pageSize) {
    return data4LibraryServiceApi.searchBooks(isbn13, keyword, publisher, pageNo, pageSize, authKey,
        format);
  }

  public GetBookRecommendationResponse getBookRecommendation(String isbn13) {
    return data4LibraryServiceApi.getBookRecommendation(isbn13, authKey, format);
  }

  public SearchLibrariesResponse searchLibraries(long isbn, int region, int dtl_region) {
    return data4LibraryServiceApi.searchLibraries(isbn, region, dtl_region, authKey, format);
  }
}
