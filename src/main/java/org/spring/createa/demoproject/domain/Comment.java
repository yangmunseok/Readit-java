package org.spring.createa.demoproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Data
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;
  String isbn13;
  String content;
  int likes;
  int score;

  @CreatedDate
  Date createdAt;

  @UpdateTimestamp
  Date updatedAt;

  @ManyToMany
  List<User> liker;

  @ManyToOne
  User commenter;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return id == comment.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Comment{" +
        "id=" + id +
        ", isbn13='" + isbn13 + '\'' +
        ", content='" + content + '\'' +
        ", likes=" + likes +
        ", score=" + score +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", commenter=" + commenter +
        '}';
  }
}
