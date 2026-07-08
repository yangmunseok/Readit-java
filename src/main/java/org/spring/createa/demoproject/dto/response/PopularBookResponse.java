package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

public record PopularBookResponse(
    Response response
) {

  public record Response(
      List<Doc> docs
  ) {

  }


  public record Doc(
      RankedBook doc
  ) {

  }

  // Book + 확장 정보
  public record RankedBook(
      int no,
      int ranking,
      String bookname,
      String authors,
      String publisher,
      String publication_year,
      String isbn13,
      String addition_symbol,
      String vol,
      String class_no,
      String class_nm,
      String bookImageURL,
      String bookDtlUrl
  ) {

    public BookDTO toBook() {
      return new BookDTO(no, bookname, authors, publisher, null, publication_year, isbn13,
          addition_symbol, vol, class_no, class_nm, null, bookImageURL, ranking);
    }
  }
}

