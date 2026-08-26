package org.spring.createa.demoproject.exception;

public class DuplicateUserException extends RuntimeException {

  public DuplicateUserException(String message) {
    super(message);
  }
}
