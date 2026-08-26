package org.spring.createa.demoproject.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.exception.DuplicateUserException;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AuthController {

  private final UserService userService;

  private static final String LOGIN_VIEW = "login";

  private static final String SIGNUP_VIEW = "signup";

  public AuthController(UserService userService) {
    this.userService = userService;
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
      String password = user.getPassword();
      userService.register(user);
      request.login(user.getName(), password);
    } catch (DuplicateUserException e) {
      //회원가입 실패시 회원가입 실패한 이유가 화면에 출력되게 한다.
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      //회원가입이 실패했을 때 실패한 유저네임과 이메일을 화원가입창에 유지시킨다.
      redirectAttributes.addFlashAttribute("name", user.getName());
      redirectAttributes.addFlashAttribute("email", user.getEmail());

      return "redirect:/signup";
    } catch (ServletException e) {
      log.error(e.getMessage(), e);
    }
    return "redirect:/";
  }


  @GetMapping("/login")
  public String login() {
    return LOGIN_VIEW;
  }

  @GetMapping("/signup")
  public String signup(@RequestParam(required = false) String email,
      @RequestParam(required = false) String name) {
    return SIGNUP_VIEW;
  }
}
