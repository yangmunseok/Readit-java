package org.spring.createa.demoproject.Repository;

import java.util.List;
import org.spring.createa.demoproject.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

  Comment findCommentById(int id);

  @Query("select c from Comment c left join fetch c.commenter left join fetch c.liker where c.id=:id")
  Comment findCommentByIdWithCommenterAndLiker(int id);

  List<Comment> findCommentsByIsbn13(String isbn13);

  int deleteCommentById(int id);
}
