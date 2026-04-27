package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.Book;

public record GetBookRecommendationResponse(Response response) {

  public record Response(List<Doc> docs) {

  }

  public record Doc(Book book) {

  }

}
