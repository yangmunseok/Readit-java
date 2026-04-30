package org.spring.createa.demoproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.service.Data4LibraryServiceAdapter;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

  private final UserService userService;
  private final UserDetailsService userDetailsService;
  private final Data4LibraryServiceAdapter data4LibraryServiceAdapter;
  @Value("${api.data4library.key}")
  String authKey;

  public MainController(UserService userService, UserDetailsService userDetailsService,
      Data4LibraryServiceAdapter data4LibraryServiceAdapter) {
    this.userService = userService;
    this.userDetailsService = userDetailsService;
    this.data4LibraryServiceAdapter = data4LibraryServiceAdapter;
  }

  @GetMapping("/findUser")
  @ResponseBody
  public User findUser(@RequestParam int id) {
    return userService.findUserById(id);
  }

  @GetMapping("/test")
  @ResponseBody
  public String test() {
    data4LibraryServiceAdapter.getPopularBooks(authKey);
    return "alive!";
  }

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "hello";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute User user, HttpServletRequest request) {
    userService.register(user);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());

    Authentication auth = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);

    HttpSession session = request.getSession(true);
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        context
    );
    return "redirect:/home";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("signup")
  public String signup() {
    return "signup";
  }
}
