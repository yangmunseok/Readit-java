package org.spring.createa.demoproject.service;

import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    return userRepository.save(user);
  }

  public User findByEmailAndProvider(String email, String provider) {
    return userRepository.findUserByEmailAndProvider(email, provider);
  }

  public User findUserById(int id) {
    return userRepository.findUserById(id);
  }
}
