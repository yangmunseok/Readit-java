package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

public record SearchDetailResponse(Response response) {

  public record Response(List<Detail> detail) {

  }

  public record Detail(BookDTO book) {

  }
}
