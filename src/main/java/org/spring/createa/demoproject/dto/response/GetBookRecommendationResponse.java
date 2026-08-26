package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

public record GetBookRecommendationResponse(Response response) {

  public record Response(List<Doc> docs) {

  }

  public record Doc(BookDTO book) {

  }

  public List<BookDTO> books() {
    return this.response.docs.stream().map(Doc::book).toList();
  }

}
