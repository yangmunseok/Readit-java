package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.dto.response.GetBookRecommendationResponse;
import org.spring.createa.demoproject.dto.response.PopularBookResponse;
import org.spring.createa.demoproject.dto.response.SearchBooksResponse;
import org.spring.createa.demoproject.dto.response.SearchDetailResponse;
import org.spring.createa.demoproject.dto.response.SearchLibrariesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class Data4LibraryServiceAdapter {

  Data4LibraryService data4LibraryService;

  @Value("${api.data4library.key}")
  String authKey;
  String format = "json";

  public Data4LibraryServiceAdapter(Data4LibraryService data4LibraryService) {
    this.data4LibraryService = data4LibraryService;
  }


  public SearchDetailResponse getBookByIsbn(String isbn13) {
    return data4LibraryService.getBookByIsbn(isbn13, authKey, format);
  }

  @Cacheable(value = "books", key = "#kdc")
  public PopularBookResponse getPopularBooks(Integer pageSize,
      String kdc) {
    if ("all".equals(kdc) || kdc == null) {
      kdc = "";
    }
    return data4LibraryService.getPopularBooks(pageSize, kdc, authKey, format);
  }

  @CachePut(value = "books", key = "#kdc")
  public PopularBookResponse cachingGetPopularBooks(Integer pageSize,
      String kdc) {
    if ("all".equals(kdc) || kdc == null) {
      kdc = "";
    }
    return data4LibraryService.getPopularBooks(pageSize, kdc, authKey, format);
  }

  public SearchBooksResponse searchBooks(String isbn13,
      List<String> keyword,
      String publisher,
      Integer pageNo,
      Integer pageSize) {
    return data4LibraryService.searchBooks(isbn13, keyword, publisher, pageNo, pageSize, authKey,
        format);
  }

  public GetBookRecommendationResponse getBookRecommendation(String isbn13) {
    return data4LibraryService.getBookRecommendation(isbn13, authKey, format);
  }

  public SearchLibrariesResponse searchLibraries(long isbn, int region) {
    return data4LibraryService.searchLibraries(isbn, region, authKey, format);
  }
}
