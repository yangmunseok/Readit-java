package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

@Deprecated
public record NaverSearchBookResponse(int total, List<Item> items) {

  public record Item(String title, String image, String author, String publisher,
                     String description,
                     String isbn) {

    public BookDTO toBook() {
      return new BookDTO(null, title, author, publisher, null, null, isbn, null, null, null, null,
          description, image, null);
    }
  }

}
