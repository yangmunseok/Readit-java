package org.spring.createa.demoproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String password;
  private String email;
  private String name;
  private String role;
  private String provider = "local";

  public User() {
  }

  public User(String email, String name, String role, String provider) {
    this.email = email;
    this.name = name;
    this.role = role;
    this.provider = provider;
  }
}
