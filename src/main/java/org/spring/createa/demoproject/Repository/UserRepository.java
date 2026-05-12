package org.spring.createa.demoproject.Repository;

import org.spring.createa.demoproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

  User findUserById(int id);

  User findUserByName(String name);

  User findUserByEmailAndProvider(String email, String provider);

  User findUserByEmailOrName(String email, String name);
}
