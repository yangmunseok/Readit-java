package org.spring.createa.demoproject.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.dto.Library;
import org.spring.createa.demoproject.dto.UserPrincipal;
import org.spring.createa.demoproject.service.CommentService;
import org.spring.createa.demoproject.service.ExternalBookService;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
public class BookController {

  private final CommentService commentService;
  ExternalBookService externalBookService;
  UserService userService;

  private final String LIBRARY_COMPONENT = "book-detail :: #available-libraries";

  @Autowired
  public BookController(UserService userService,
      CommentService commentService, ExternalBookService externalBookService) {
    this.externalBookService = externalBookService;
    this.userService = userService;
    this.commentService = commentService;
  }

  @ModelAttribute
  User populateUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return userPrincipal.getUser();
  }

  @GetMapping("/")
  String home(@ModelAttribute User user, Model model) {
    List<BookDTO> recommendBooks = externalBookService.getRecommendations(user.getBookOfInterest());
    List<BookDTO> popularBooks = externalBookService.getTop100LoanedBooks(Category.TOTAL)
        .subList(0, 6);
    model.addAttribute("recommendedBooks", recommendBooks);
    model.addAttribute("bookRanks", popularBooks);
    return "home";
  }

  @GetMapping("/books/{isbn}")
  String getBookByIsbn(@ModelAttribute User user, @PathVariable String isbn,
      Model model) {
    try {
      BookDTO book = externalBookService.findBookByIsbn(isbn);

      model.addAttribute("book", book);
      model.addAttribute("libraries", new ArrayList<Library>());
      model.addAttribute("reviews", commentService.findCommentsByIsbn13(isbn));

      user.addSearchHistory(isbn);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return "book-detail";
  }

  @GetMapping("/books/popular")
  String getPopularBooks(@RequestParam(required = false) String category, Model model) {

    List<BookDTO> popularBooks = externalBookService.getTop100LoanedBooks(
        Category.from(category).orElse(Category.TOTAL));

    model.addAttribute("categories", Category.valuesList());
    model.addAttribute("currentCategory", category);
    model.addAttribute("books", popularBooks);
    return "book-popular";
  }

  @GetMapping("/books")
  String searchBook(@RequestParam(required = false) List<String> keyword,
      @RequestParam String query,
      @RequestParam(required = false) Integer pageNo,
      @RequestParam(required = false) Integer pageSize,
      Model model) {
    pageNo = pageNo == null ? 1 : pageNo;
    pageSize = pageSize == null ? 20 : pageSize;

    List<BookDTO> books = externalBookService.searchBooks(query, pageNo, pageSize);

    model.addAttribute("books", books);
    model.addAttribute("numbers",
        IntStream.range(1, (books.size() - 1) / pageSize + 2).toArray());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("keyword", keyword);

    return "book-search";
  }

  @GetMapping("/libraries")
  String searchLibraries(@RequestParam String isbn, @RequestParam String region,
      @RequestParam String dtlRegion, Model model) {
    List<Library> libraries = externalBookService.findOwnerLibraries(isbn, region, dtlRegion);
    model.addAttribute("libraries", libraries);
    return LIBRARY_COMPONENT;
  }
}
