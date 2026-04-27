package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.dto.response.GetBookRecommendationResponse;
import org.spring.createa.demoproject.dto.response.PopularBookResponse;
import org.spring.createa.demoproject.dto.response.SearchBooksResponse;
import org.spring.createa.demoproject.dto.response.SearchDetailResponse;
import org.spring.createa.demoproject.dto.response.SearchLibrariesResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange(value = "http://data4library.kr/api/")
public interface Data4LibraryService {

  @GetExchange("/srchDtlList")
  SearchDetailResponse getBookByIsbn(@RequestParam String isbn13, @RequestParam String authKey,
      @RequestParam String format);

  @GetExchange("/loanItemSrch")
  PopularBookResponse getPopularBooks(@RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) String kdc, @RequestParam String authKey,
      @RequestParam String format);

  @GetExchange("/srchBooks?exactMatch=true")
  SearchBooksResponse searchBooks(@RequestParam(required = false) String isbn13,
      @RequestParam(required = false) List<String> keyword,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) Integer pageNo,
      @RequestParam(required = false) Integer pageSize,
      @RequestParam String authKey, @RequestParam String format);

  @GetExchange("/recommandList")
  GetBookRecommendationResponse getBookRecommendation(@RequestParam String isbn13,
      @RequestParam String authKey, @RequestParam String format);

  @GetExchange("/libSrchByBook")
  SearchLibrariesResponse searchLibraries(@RequestParam long isbn, @RequestParam int region,
      @RequestParam String authKey, @RequestParam String format);
}