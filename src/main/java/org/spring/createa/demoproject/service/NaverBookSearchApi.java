package org.spring.createa.demoproject.service;

import org.spring.createa.demoproject.dto.response.NaverSearchBookResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange(value = "https://openapi.naver.com/v1")
public interface NaverBookSearchApi {

  @GetExchange("/search/book.json")
  NaverSearchBookResponse searchBooks(@RequestParam String query,
      @RequestParam(required = false) Integer start,
      @RequestParam(required = false) Integer display,
      @RequestHeader("X-Naver-Client-Id") String clientId,
      @RequestHeader("X-Naver-Client-Secret") String clientSecret
  );

}
