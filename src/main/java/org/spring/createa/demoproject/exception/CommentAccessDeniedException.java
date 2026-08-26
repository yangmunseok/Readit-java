package org.spring.createa.demoproject.exception;

public class CommentAccessDeniedException extends RuntimeException {

  public CommentAccessDeniedException(String message) {
    super(message);
  }
}
