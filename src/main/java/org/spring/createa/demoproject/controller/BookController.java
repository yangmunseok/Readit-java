package org.spring.createa.demoproject.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.spring.createa.demoproject.domain.BookRanking;
import org.spring.createa.demoproject.domain.BookRanking.Category;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.dto.Library;
import org.spring.createa.demoproject.dto.UserPrincipal;
import org.spring.createa.demoproject.dto.response.SearchLibrariesResponse;
import org.spring.createa.demoproject.service.BookRankingService;
import org.spring.createa.demoproject.service.CommentService;
import org.spring.createa.demoproject.service.Data4LibraryService;
import org.spring.createa.demoproject.service.NaverBookSearchApi;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BookController {

  private final CommentService commentService;
  private final BookRankingService bookRankingService;
  Data4LibraryService data4LibraryService;
  NaverBookSearchApi naverBookSearchApi;
  UserService userService;

  @Value("${api.naverlibrary.client.id}")
  String NAVER_LIBRARY_API_ID;

  @Value("${api.naverlibrary.client.secret}")
  String NAVER_LIBRARY_API_SECRET;

  @Autowired
  public BookController(Data4LibraryService data4LibraryService,
      UserService userService,
      CommentService commentService, NaverBookSearchApi naverBookSearchApi,
      BookRankingService bookRankingService) {
    this.data4LibraryService = data4LibraryService;
    this.naverBookSearchApi = naverBookSearchApi;
    this.userService = userService;
    this.commentService = commentService;
    this.bookRankingService = bookRankingService;
  }


  @GetMapping("/")
  String home(@AuthenticationPrincipal UserPrincipal user, Model model) {

    User currentUser = user.getUser();
    List<BookDTO> recommendBooks = getRecommendedBooks(currentUser.getBookOfInterest());
    List<BookDTO> popularBooks = bookRankingService.getPopularBooks(6, BookRanking.Category.TOTAL);
    model.addAttribute("recommendedBooks", recommendBooks);
    model.addAttribute("bookRanks", popularBooks);
    model.addAttribute("user", currentUser);
    return "home";
  }

  @GetMapping("/books/{isbn}")
  String getBookByIsbn(@AuthenticationPrincipal UserPrincipal user, @PathVariable String isbn,
      Model model) {
    try {
      BookDTO book = data4LibraryService.getBookByIsbn(isbn).response().detail()
          .getFirst()
          .book();
      model.addAttribute("book", book);
      model.addAttribute("libraries", new ArrayList<Library>());
      model.addAttribute("reviews", commentService.findCommentsByIsbn13(isbn));
      model.addAttribute("user", user.getUser());
      userService.updateBookOfInterest(user.getUser(), isbn);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return "book-detail";
  }

  @GetMapping("/books/popular")
  String getPopularBooks(@RequestParam(required = false) String category, Model model) {
    Map<String, BookRanking.Category> categories = Map.ofEntries(
        Map.entry("전체", Category.TOTAL),
        Map.entry("총류", Category.GENERAL_WORK),
        Map.entry("철학", Category.PHILOSOPHY),
        Map.entry("종교", Category.RELIGION),
        Map.entry("사회과학", Category.SOCIAL_SCIENCE),
        Map.entry("자연과학", Category.NATURAL_SCIENCE),
        Map.entry("기술과학", Category.TECHNOLOGY),
        Map.entry("예술", Category.ART),
        Map.entry("언어", Category.LANGUAGE),
        Map.entry("문학", Category.LITERATURE),
        Map.entry("역사", Category.HISTORY)
    );
    List<String> categoriesList = List.of("전체",
        "총류",
        "철학",
        "종교",
        "사회과학",
        "자연과학",
        "기술과학",
        "예술",
        "언어",
        "문학",
        "역사");

    if (category == null || categories.get(category) == null) {
      category = "전체";
    }
    List<BookDTO> popularBooks = bookRankingService.getPopularBooks(100, categories.get(category));

    model.addAttribute("categories", categoriesList);
    model.addAttribute("currentCategory", category);
    model.addAttribute("books", popularBooks);
    return "book-popular";
  }

  List<BookDTO> getRecommendedBooks(List<String> isbn) {
    if (isbn.isEmpty()) {
      return new ArrayList<>();
    }
    return data4LibraryService.getBookRecommendation(
            isbn.stream().reduce("", (a, b) -> a + ";" + b)).response().docs().stream()
        .map(doc -> doc.book()).toList();
  }

  @GetMapping("/books")
  String searchBook(@RequestParam(required = false) String isbn13,
      @RequestParam(required = false) List<String> keyword,
      @RequestParam String query,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) Integer pageNo,
      @RequestParam(required = false) Integer pageSize,
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      Model model) {

    /*
    var searchResult = data4LibraryServiceAdapter.searchBooks(isbn13, keyword, publisher, pageNo,
        pageSize).response();

    List<Book> books = searchResult.docs().stream().map(
        BookDoc::doc).toList();
    model.addAttribute("books", books);
  */

    if (pageNo == null) {
      pageNo = 1;
    }
    if (pageSize == null) {
      pageSize = 20;
    }

    int start = pageSize * (pageNo - 1) + 1;

    var naverSearchResult = naverBookSearchApi.searchBooks(query, start, pageSize,
        NAVER_LIBRARY_API_ID, NAVER_LIBRARY_API_SECRET);
    model.addAttribute("books", naverSearchResult.items());

    model.addAttribute("numbers",
        IntStream.range(1, (naverSearchResult.total() - 1) / pageSize + 2).toArray());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("keyword", keyword);
    model.addAttribute("user", userPrincipal.getUser());
    model.addAttribute("reviews", commentService.findCommentsByIsbn13(isbn13));
    return "book-search";
  }

  @GetMapping("/libraries")
  String searchLibraries(@RequestParam long isbn, @RequestParam int region,
      @RequestParam Integer dtlRegion, Model model) {
    List<Library> libraries = data4LibraryService.searchLibraries(isbn, region, dtlRegion)
        .response().libs().stream().map(SearchLibrariesResponse.Doc::lib).toList();
    model.addAttribute("libraries", libraries);
    return "book-detail :: #available-libraries";
  }
}
