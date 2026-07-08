package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

public record SearchBooksResponse(
    Response response
) {

  public record Response(
      int numFound,
      List<BookDoc> docs
  ) {

  }

  public record BookDoc(
      BookDTO doc
  ) {

  }
}

