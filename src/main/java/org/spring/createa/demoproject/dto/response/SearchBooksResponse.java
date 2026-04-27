package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.Book;

public record SearchBooksResponse(
    Response response
) {

  public record Response(
      int numFound,
      List<BookDoc> docs
  ) {

  }

  public record BookDoc(
      Book doc
  ) {

  }
}

