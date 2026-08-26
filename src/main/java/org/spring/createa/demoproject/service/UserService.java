package org.spring.createa.demoproject.service;

import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.exception.DuplicateUserException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder bCryptPasswordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public User register(User user) {
    try {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      if (userRepository.existsByEmail(user.getEmail())) {
        throw new DuplicateUserException("이미 가입된 이메일입니다.");
      }
      throw new DuplicateUserException("이미 사용된 유저네임입니다.");
    }
  }

  public User findByEmailAndProvider(String email, String provider) {
    return userRepository.findUserByEmailAndProvider(email, provider);
  }

}
