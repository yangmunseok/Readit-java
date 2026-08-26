package org.spring.createa.demoproject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.createa.demoproject.Repository.CommentRepository;
import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.exception.DuplicateUserException;
import org.spring.createa.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class UnitTest {

  @Autowired
  UserRepository userRepository;
  @Autowired
  CommentRepository commentRepository;

  @Autowired
  UserService userService;

  @MockitoBean
  PasswordEncoder passwordEncoder;
  User user = new User("tester@mail.com", "name", "user", "local", "pass");
  User anotherUserWithSameEmail = new User("tester@mail.com", "newname", "user", "local", "pass");
  User anotherUserWithSameUsername = new User("tester@newmail.com", "name", "user", "local",
      "pass");

  @BeforeEach
  void clearDB() {
    commentRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void passwordEncoded() {

    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    User dbUser = userRepository.findUserByName("name");

    assertThat(dbUser.getPassword()).isEqualTo("encoded");
  }

  @Test
  void userSaved() {

    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    User dbUser = userRepository.findUserByName("name");

    assertThat(dbUser).usingRecursiveAssertion().ignoringFields("password").isEqualTo(user);
  }

  @Test
  void testFindByEmailAndProvider() {
    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    User dbUser = userService.findByEmailAndProvider("tester@mail.com", "local");
    assertThat(dbUser).usingRecursiveAssertion().ignoringFields("password").isEqualTo(user);
  }

  @Test
  void userSavedNotNull() {
    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    User dbUser = userService.findByEmailAndProvider("tester@mail.com", "google");
    assertThat(dbUser).isNull();
  }

  @Test()
  void checkEmailDuplication() {
    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    assertThatThrownBy(() -> userService.register(anotherUserWithSameEmail)).isInstanceOf(
        DuplicateUserException.class).hasMessage("이미 가입된 이메일입니다.");
  }

  @Test
  void checkUsernameDuplication() {
    when(passwordEncoder.encode("pass")).thenReturn("encoded");

    userService.register(user);

    assertThatThrownBy(() -> userService.register(anotherUserWithSameUsername)).isInstanceOf(
        DuplicateUserException.class).hasMessage("이미 사용된 유저네임입니다.");
  }
}
