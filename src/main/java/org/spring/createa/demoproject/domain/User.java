package org.spring.createa.demoproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String password;

  @Column(unique = true)
  private String email;
  @Column(unique = true)
  private String name;
  private String role;
  private String provider = "local";
  private String bookOfInterest;
  @OneToMany(mappedBy = "commenter")
  private List<Comment> comment = new ArrayList<>();

  @ManyToMany(mappedBy = "liker")
  private Set<Comment> like = new HashSet<>();

  public User() {
  }

  public User(String email, String name, String role, String provider, String password) {
    this.email = email;
    this.name = name;
    this.role = role;
    this.provider = provider;
    this.password = password;
  }

  public void addSearchHistory(String isbn) {
    if (bookOfInterest == null) {
      bookOfInterest = isbn;
    }
    if (bookOfInterest.contains(isbn)) {
      return;
    }
    bookOfInterest += "," + isbn;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", role='" + role + '\'' +
        ", provider='" + provider + '\'' +
        ", bookOfInterest=" + bookOfInterest +
        '}';
  }
}
