package org.spring.createa.demoproject.service;

import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.dto.Library;
import org.spring.createa.demoproject.dto.response.SearchLibrariesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExternalBookServiceImpl implements ExternalBookService {

  Data4LibraryServiceApi data4LibraryServiceApi;
  NaverBookSearchApi naverBookSearchApi;
  KakaoBookSearchApi kakaoBookSearchApi;
  LoadingCache<Category, List<BookDTO>> bookCache;

  @Value("${api.naverlibrary.client.id}")
  String NAVER_LIBRARY_API_ID;

  @Value("${api.naverlibrary.client.secret}")
  String NAVER_LIBRARY_API_SECRET;

  @Value("${api.data4library.key}")
  String authKey;

  @Value("KakaoAK ${api.kakao.key}")
  String authorization;

  String format = "json";

  public ExternalBookServiceImpl(Data4LibraryServiceApi data4LibraryServiceApi,
      NaverBookSearchApi naverBookSearchApi, KakaoBookSearchApi kakaoBookSearchApi,
      LoadingCache<Category, List<BookDTO>> bookCache) {
    this.data4LibraryServiceApi = data4LibraryServiceApi;
    this.naverBookSearchApi = naverBookSearchApi;
    this.kakaoBookSearchApi = kakaoBookSearchApi;
    this.bookCache = bookCache;
  }

  @Override
  public List<BookDTO> getRecommendations(String isbn) {
    if (isbn == null || isbn.isEmpty()) {
      return new ArrayList<>();
    }
    return data4LibraryServiceApi.getBookRecommendation(
        Arrays.stream(isbn.split(",")).reduce("", (a, b) -> a + ";" + b),
        authKey,
        format).books();
  }

  @Override
  public List<Library> findOwnerLibraries(String isbn, String region, String dtlRegion) {
    return data4LibraryServiceApi.searchLibraries(isbn, region, dtlRegion, authKey, format)
        .response()
        .libs()
        .stream()
        .map(SearchLibrariesResponse.Doc::lib)
        .toList();
  }

  @Override
  public List<BookDTO> searchBooks(String query, Integer pageNo, Integer pageSize) {
    pageNo = pageNo == null ? 1 : pageNo;
    pageSize = pageSize == null ? 20 : pageSize;
    return kakaoBookSearchApi.searchBook(query, pageNo, pageSize, authorization).books();
  }

  @Override
  public BookDTO findBookByIsbn(String isbn) {
    return data4LibraryServiceApi.getBookByIsbn(isbn, authKey, format).books().getFirst();
  }

  @Override
  public List<BookDTO> getTop100LoanedBooks(Category category) {
    return bookCache.get(category);
  }
}
