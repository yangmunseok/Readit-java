package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.Book;

public record SearchDetailResponse(Response response) {

  public record Response(List<Detail> detail) {

  }

  public record Detail(Book book) {

  }
}
