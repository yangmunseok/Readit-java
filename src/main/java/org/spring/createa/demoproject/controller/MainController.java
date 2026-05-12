package org.spring.createa.demoproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

  private final UserService userService;
  private final UserDetailsService userDetailsService;


  public MainController(UserService userService, UserDetailsService userDetailsService) {
    this.userService = userService;
    this.userDetailsService = userDetailsService;
  }

  @GetMapping("/findUser")
  @ResponseBody
  public User findUser(@RequestParam int id) {
    return userService.findUserById(id);
  }

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "hello";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute User user, HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    try {
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
    } catch (DataIntegrityViolationException e) {
      redirectAttributes.addFlashAttribute("message",
          e.getMessage());
      redirectAttributes.addFlashAttribute("name", user.getName());
      redirectAttributes.addFlashAttribute("email", user.getEmail());
      return "redirect:/signup";
    }
    return "redirect:/";
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
