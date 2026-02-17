package org.spring.createa.demoproject.controller;

import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

  private final UserService userService;

  @Autowired
  public MainController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("/hello")
  @ResponseBody
  public String helloWorld() {
    return "hello world!";
  }


  @GetMapping("/user")
  @ResponseBody
  public String printUser(@AuthenticationPrincipal Object principal) {

    System.out.println(principal);
    return "user";
  }

  @GetMapping("/findUser")
  @ResponseBody
  public User findUser(@RequestParam int id) {
    return userService.findUserById(id);
  }

  @ResponseBody
  @PostMapping("/register")
  public User register(@RequestBody User user) {
    return userService.register(user);
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
