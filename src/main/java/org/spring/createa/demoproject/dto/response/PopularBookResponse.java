package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.Book;

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
      String ranking,
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
      String bookDtlUrl,
      String loan_count
  ) {

    /**
     * 기존 Book record로 변환해 쓰고 싶을 때 사용.
     */
    public Book toBook() {
      return new Book(
          no,
          bookname,
          authors,
          publisher,
          null,
          publication_year,
          null,            // 기존 Book의 isbn
          isbn13,
          addition_symbol,
          vol,
          class_no,
          class_nm,
          null,            // description은 이 API에 없음
          bookImageURL
      );
    }
  }
}

