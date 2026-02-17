package org.spring.createa.demoproject.service;

import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.UserPrinipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailService implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public AppUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByName(username);
    if (user == null) {
      throw new UsernameNotFoundException("username not found");
    }

    return UserPrinipal.builder().user(user).name(user.getName()).build();
  }
}
