package org.spring.createa.demoproject.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.service.BookRankingProvider;
import org.spring.createa.demoproject.service.ExternalBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

  @Bean
  LoadingCache<Category, List<BookDTO>> bookRankCache(
      @Autowired BookRankingProvider bookRankingProvider) {
    return Caffeine
        .newBuilder()
        .maximumSize(50)
        .refreshAfterWrite(Duration.ofDays(1))
        .build((category) -> {
          log.info("Cache Miss : {}", category);
          return bookRankingProvider.getTop100LoanedBooks(category);
        });
  }


  @Bean
  CommandLineRunner init(@Autowired ExternalBookService externalBookService) {
    return args -> {
      log.info("init book");
      for (Category category : Category.values()) {
        externalBookService.getTop100LoanedBooks(category);
      }
    };
  }

}
