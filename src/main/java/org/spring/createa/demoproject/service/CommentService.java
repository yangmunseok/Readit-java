package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.Repository.CommentRepository;
import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.Comment;
import org.spring.createa.demoproject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  CommentRepository commentRepository;
  UserRepository userRepository;

  @Autowired
  public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  public void addComment(String isbn13, String comment, int score, User commenter) {
    Comment newComment = new Comment();
    newComment.setIsbn13(isbn13);
    newComment.setContent(comment);
    newComment.setScore(score);
    newComment.setCommenter(commenter);

    commentRepository.save(newComment);
  }

  @Transactional
  public void likeOrUnlikeComment(int commentId, User user) {
    Comment comment = commentRepository.findCommentById(commentId);
    if (comment.getLiker().contains(user)) {
      comment.setLikes(comment.getLikes() - 1);
      comment.getLiker().remove(user);
      commentRepository.save(comment);
      return;
    }
    comment.setLikes(comment.getLikes() + 1);
    comment.getLiker().add(user);
    commentRepository.save(comment);
  }

  public List<Comment> findCommentsByIsbn13(String isbn13) {
    return commentRepository.findCommentsByIsbn13(isbn13);
  }

  @Transactional
  public int deleteCommentById(Comment exComment, User user) {
    if (!exComment.getCommenter().equals(user)) {
      return -1;
    }
    return commentRepository.deleteCommentById(exComment.getId());
  }

  @Transactional
  public int patchComment(Comment exComment, String content, Integer score, User user) {
    if (!exComment.getCommenter().equals(user)) {
      return -1;
    }
    if (content != null) {
      exComment.setContent(content);
    }
    if (score != null && score > 0 && score < 6) {
      exComment.setScore(score);
    }

    commentRepository.save(exComment);
    return 1;
  }

  public Comment findCommentById(int id) {
    return commentRepository.findCommentById(id);
  }
}
