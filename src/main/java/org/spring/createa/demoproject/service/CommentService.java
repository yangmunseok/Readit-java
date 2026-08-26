package org.spring.createa.demoproject.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.spring.createa.demoproject.Repository.CommentRepository;
import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.Comment;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.exception.CommentAccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CommentService {

  CommentRepository commentRepository;
  UserRepository userRepository;

  @Autowired
  public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  public Comment addComment(String isbn13, String comment, int score, User commenter) {
    Comment newComment = new Comment(isbn13, comment, score, commenter);
    return commentRepository.save(newComment);
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
  public void deleteCommentById(int commentId, User user) {
    Comment exComment = commentRepository.findCommentById(commentId);
    if (!exComment.getCommenter().equals(user)) {
      throw new CommentAccessDeniedException("다른 사람의 댓글을 삭제할 수 없습니다.");
    }
    commentRepository.deleteCommentById(exComment.getId());
  }

  @Transactional
  public Comment patchComment(int commentId, String content,
      @Min(value = 1, message = "별점은 최소 1점이어야 합니다.")
      @Max(value = 5, message = "별점은 최대 5점이어야 합니다.") Integer score, User user) {
    Comment exComment = commentRepository.findCommentById(commentId);
    if (!exComment.getCommenter().equals(user)) {
      throw new CommentAccessDeniedException("다른 사람의 댓글을 변경할 수 없습니다.");
    }

    content = (content == null) ? exComment.getContent() : content;
    score = (score == null) ? exComment.getScore() : score;

    exComment.setContent(content);
    exComment.setScore(score);

    return commentRepository.save(exComment);
  }

  public Comment findCommentById(int id) {
    return commentRepository.findCommentById(id);
  }

  public Comment findCommentByIdWithCommenterAndLiker(int id) {
    return commentRepository.findCommentByIdWithCommenterAndLiker(id);
  }
}
