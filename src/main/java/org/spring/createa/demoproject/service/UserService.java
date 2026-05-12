package org.spring.createa.demoproject.service;

import java.util.ArrayList;
import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
    bCryptPasswordEncoder = new BCryptPasswordEncoder(5);
  }

  public User register(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    if (userRepository.findUserByEmailOrName(user.getEmail(), user.getName()) == null) {
      return userRepository.save(user);
    }
    if (userRepository.findUserByName(user.getName()) == null) {
      throw new DataIntegrityViolationException("이미 가입된 이메일입니다. 다른 이메일 주소로 바꿔주세요.");
    }
    throw new DataIntegrityViolationException("중복된 계정이름입니다. 계정이름을 바꿔주세요");
  }

  public User findByEmailAndProvider(String email, String provider) {
    return userRepository.findUserByEmailAndProvider(email, provider);
  }

  public User findUserById(int id) {
    return userRepository.findUserById(id);
  }

  public void updateBookOfInterest(User user, String isbn13) {
    ArrayList<String> books = (ArrayList<String>) user.getBookOfInterest();
    if (!books.contains(isbn13)) {
      books.add(isbn13);
    }
    if (books.size() > 5) {
      books.removeFirst();
    }
    user.setBookOfInterest(books);
    userRepository.save(user);
  }

}
