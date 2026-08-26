package org.spring.createa.demoproject.dto.response;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.spring.createa.demoproject.dto.BookDTO;

public record KakaoSearchBookResponse(List<Doc> documents) {

  public record Doc(List<String> authors, String contents, String isbn, String publisher,
                    String thumbnail, String title, OffsetDateTime datetime) {

    public BookDTO toDto() {
      return new BookDTO(null, title, String.join(",", authors), publisher,
          datetime.toLocalDate().toString(), Integer.toString(datetime.getYear()),
          Arrays.stream(isbn.split(" ")).toList().getLast(), null, null, null, null, contents,
          thumbnail, null);
    }
  }

  public List<BookDTO> books() {
    return this.documents.stream().map(Doc::toDto).toList();
  }
}
