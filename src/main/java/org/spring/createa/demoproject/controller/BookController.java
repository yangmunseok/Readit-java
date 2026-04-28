package org.spring.createa.demoproject.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.Book;
import org.spring.createa.demoproject.dto.Library;
import org.spring.createa.demoproject.dto.UserPrincipal;
import org.spring.createa.demoproject.dto.response.PopularBookResponse.RankedBook;
import org.spring.createa.demoproject.dto.response.SearchBooksResponse.BookDoc;
import org.spring.createa.demoproject.service.CommentService;
import org.spring.createa.demoproject.service.Data4LibraryService;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

  private Logger logger = Logger.getLogger(BookController.class.getName());

  private final CommentService commentService;
  Data4LibraryService data4LibraryService;
  UserService userService;
  @Value("${api.data4library.key}")
  String authKey;

  @Autowired
  public BookController(Data4LibraryService data4LibraryService, UserService userService,
      CommentService commentService) {
    this.data4LibraryService = data4LibraryService;
    this.userService = userService;
    this.commentService = commentService;
  }


  @GetMapping("/home")
  String home(@AuthenticationPrincipal UserPrincipal user, Model model) {

    User currentUser = user.getUser();
    List<Book> recommendBooks = getRecommendedBooks(currentUser.getBookOfInterest());
    List<RankedBook> popularBooks = data4LibraryService.getPopularBooks(6, null, authKey, "json")
        .response()
        .docs().stream()
        .map(doc -> doc.doc()).toList();
    model.addAttribute("recommendedBooks", recommendBooks);
    model.addAttribute("bookRanks", popularBooks);
    model.addAttribute("user", currentUser);
    return "home";
  }

  @GetMapping("/books/{isbn}")
  String getBookByIsbn(@AuthenticationPrincipal UserPrincipal user, @PathVariable String isbn,
      Model model) {
    Book book = data4LibraryService.getBookByIsbn(isbn, authKey, "json").response().detail()
        .getFirst()
        .book();
    model.addAttribute("book", book);
    model.addAttribute("libraries", new ArrayList<Library>());
    model.addAttribute("reviews", commentService.findCommentsByIsbn13(isbn));
    model.addAttribute("user", user.getUser());
    userService.updateBookOfInterest(user.getUser(), isbn);
    return "book-detail";
  }

  @GetMapping("/books/popular")
  String getPopularBooks(@RequestParam(required = false) String category, Model model) {
    Map<String, String> categories = Map.ofEntries(
        Map.entry("전체", ""),
        Map.entry("총류", "0"),
        Map.entry("철학", "1"),
        Map.entry("종교", "2"),
        Map.entry("사회과학", "3"),
        Map.entry("자연과학", "4"),
        Map.entry("기술과학", "5"),
        Map.entry("예술", "6"),
        Map.entry("언어", "7"),
        Map.entry("문학", "8"),
        Map.entry("역사", "9")
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

    if (category == null) {
      category = "전체";
    }
    List<RankedBook> popularBooks = data4LibraryService.getPopularBooks(null,
            categories.get(category), authKey, "json").response().docs().stream()
        .map(doc -> doc.doc()).toList();

    model.addAttribute("categories", categoriesList);
    model.addAttribute("currentCategory", category);
    model.addAttribute("books", popularBooks);
    return "book-popular";
  }

  List<Book> getRecommendedBooks(List<String> isbn) {
    if (isbn.isEmpty()) {
      return new ArrayList<>();
    }
    return data4LibraryService.getBookRecommendation(
            isbn.stream().reduce("", (a, b) -> a + ";" + b),
            authKey, "json").response().docs().stream()
        .map(doc -> doc.book()).toList();
  }

  @GetMapping("/books")
  String searchBook(@RequestParam(required = false) String isbn13,
      @RequestParam(required = false) List<String> keyword,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) Integer pageNo,
      @RequestParam(required = false) Integer pageSize,
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      Model model) {
    var searchResult = data4LibraryService.searchBooks(isbn13, keyword, publisher, pageNo, pageSize,
        authKey,
        "json").response();

    List<Book> books = searchResult.docs().stream().map(
        BookDoc::doc).toList();
    model.addAttribute("books", books);

    if (pageNo == null) {
      pageNo = 1;
    }
    if (pageSize == null) {
      pageSize = 20;
    }

    model.addAttribute("numbers",
        IntStream.range(1, (searchResult.numFound() - 1) / pageSize + 2).toArray());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("keyword", keyword);
    model.addAttribute("user", userPrincipal.getUser());
    model.addAttribute("reviews", commentService.findCommentsByIsbn13(isbn13));
    return "book-search";
  }

  @GetMapping("/libraries")
  String searchLibraries(@RequestParam long isbn, @RequestParam int region,
      @RequestParam(required = false) Integer dtlRegion, Model model) {
    List<Library> libraries = data4LibraryService.searchLibraries(isbn, region, authKey, "json")
        .response().libs().stream().map(doc -> doc.lib()).toList();
    System.out.println(data4LibraryService.searchLibraries(isbn, region, authKey, "json"));
    if (libraries == null) {
      libraries = new ArrayList<Library>();
    }
    model.addAttribute("libraries", libraries);
    return "book-detail :: #available-libraries";
  }
}
