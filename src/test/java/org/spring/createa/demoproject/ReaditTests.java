package org.spring.createa.demoproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;

import java.io.IOException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.createa.demoproject.Repository.CommentRepository;
import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.service.ExternalBookService;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReaditTests {

  @Autowired
  WebClient webClient;

  @Autowired
  MockMvcTester mvc;

  @Autowired
  ExternalBookService externalBookService;

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;
  @Autowired
  CommentRepository commentRepository;

  @LocalServerPort
  int port;

  User user = new User("tester@mail.com", "user", "user", "local", "pass");

  @BeforeEach
  void init() {
    commentRepository.deleteAll();
    userRepository.deleteAll();
    userService.register(user);
    webClient.getOptions().setJavaScriptEnabled(false);
  }

  @Test
  @DisplayName("홈페이지 기능 테스트")
  @WithUserDetails()
  void testHomePage() throws IOException {
    HtmlPage homepage = webClient.getPage("http://localhost:" + port + "/");

    //홈페이지 컨텐츠 테스트
    bookContainerContainsSixItem(homepage);
    validateBookRankingContent(homepage);
    recommendationShouldBeInitialState(homepage);

    checkClickFormButton(homepage);
    checkRankingPageLink(homepage);
  }

  private void checkRankingPageLink(HtmlPage homepage) throws IOException {
    HtmlPage redirectPage = homepage.<HtmlAnchor>querySelector("#ranking-page-link").click();
    assertEquals("http://localhost:" + port + "/books/popular", redirectPage.getUrl().toString());
  }

  private void checkClickFormButton(HtmlPage homepage) throws IOException {
    HtmlForm form = homepage.querySelector("#search-form");
    form.getInputByName("query").type("아몬드");
    HtmlPage redirectPage = form.<HtmlButton>querySelector("button[type='submit']").click();

    String url = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/books")
        .queryParam("query", "아몬드")
        .build().encode().toUriString();
    assertEquals(
        url,
        redirectPage.getUrl().toString()
    );
    assertEquals(200, redirectPage.getWebResponse().getStatusCode());
  }

  @Test
  void testNaverApi() {
    externalBookService.searchBooks("아몬드", null, null);
  }

  private void recommendationShouldBeInitialState(HtmlPage page) {
    var recommendationContent = page.querySelectorAll("#recommendation-container div");
    assertEquals(1, recommendationContent.size());
    var recommendationDescription = recommendationContent
        .getFirst().getTextContent().trim();
    assertEquals("user님, 책의 정보를 조회하고 맞춤 추천 도서를 바로 만나보세요!",
        recommendationDescription);
  }

  @Test
  void testOidcLogin() {
    mvc.get().uri("/").with(oidcLogin()).exchange();

  }

  void bookContainerContainsSixItem(HtmlPage page) {
    assertEquals(6, page.querySelectorAll("#popular-book-container a").size());
  }

  private void validateBookRankingContent(HtmlPage page) throws IOException {
    var items = page.querySelectorAll("#popular-book-container > a");

    for (var item : items) {
      HtmlImage image = item.querySelector("img");
      assertTrue(image.getSrcAttribute()
          .matches("https?://.+\\.(jpg|jpeg|png|gif|webp|svg)(\\?.*)?$"));

      var bookRankingContent = item.querySelectorAll("div > p");

      assertEquals(2, bookRankingContent.size());

      String ranking = bookRankingContent.getFirst().getTextContent().trim();
      String description = bookRankingContent.getLast().getTextContent().trim();

      assertTrue(ranking.matches("\\d+"));
      assertFalse(description.isEmpty());
    }
    HtmlAnchor firstItem = page.querySelector("#popular-book-container > a");
    HtmlPage redirect = firstItem.click();
    assertEquals(200, redirect.getWebResponse().getStatusCode());

  }


  /*
  @TestConfiguration
  static class MyConfiguration {

    UserPrincipal userPrincipal = UserPrincipal.builder()
        .user(new User("tester@mail.com", "user", "user", "local", null))
        .name("user").build();

    @Bean
    UserDetailsService testUserDetailsService() {
      return username -> {
        if (!userPrincipal.getUsername().equals(username)) {
          throw new UsernameNotFoundException("username not found");
        }
        return userPrincipal;
      };
    }
  }
  회원가입에서 db 유저를 넣는 로직을 뺴서 시간을 절약하기 위해서 만든 테스트 세팅이다.
  하지만 그냥 인메모리 db로 테스트 하는게 나을 것 같아서 주석처리를 헀다.
  학습용으로 참고하기 위해서 기록을 남겼지만 지워도 무관.
  */
}
