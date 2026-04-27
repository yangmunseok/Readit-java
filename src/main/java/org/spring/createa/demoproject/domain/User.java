package org.spring.createa.demoproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
  private List<String> bookOfInterest;

  @OneToMany(mappedBy = "commenter")
  private List<Comment> comment;

  @ManyToMany(mappedBy = "liker")
  private List<Comment> like;

  public User() {
  }

  public User(String email, String name, String role, String provider) {
    this.email = email;
    this.name = name;
    this.role = role;
    this.provider = provider;
    this.bookOfInterest = new LinkedList<>();
    this.comment = new LinkedList<>();
    this.like = new LinkedList<>();
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
