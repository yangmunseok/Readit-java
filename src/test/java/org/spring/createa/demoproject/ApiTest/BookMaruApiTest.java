package org.spring.createa.demoproject.ApiTest;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.createa.demoproject.service.Data4LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled("현재로서는 불필요")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Data4LibraryService.class})
public class BookMaruApiTest {

  @Autowired
  Data4LibraryService bookMaruApi;

  @Value("${api.data4library.key}")
  String AUTH_KEY;

  @Test
  @DisplayName("Api 소요시간 테스트")
  void testApi() {
    assertTimeout(Duration.ofSeconds(3),
        () -> bookMaruApi.getPopularBooks(null, null, AUTH_KEY, "json"));
  }
}
