package org.spring.createa.demoproject.service;

import org.spring.createa.demoproject.dto.response.KakaoSearchBookResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange("https://dapi.kakao.com/v3")
public interface KakaoBookSearchApi {

  @GetExchange("/search/book")
  KakaoSearchBookResponse searchBook(@RequestParam String query, @RequestParam Integer page,
      @RequestParam Integer size,
      @RequestHeader String Authorization);

}
