package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class BookRankingProviderImpl implements BookRankingProvider {

  @Value("${api.data4library.key}")
  String authKey;

  String format = "json";

  Data4LibraryServiceApi data4LibraryServiceApi;

  public BookRankingProviderImpl(Data4LibraryServiceApi data4LibraryServiceApi) {
    this.data4LibraryServiceApi = data4LibraryServiceApi;
  }

  @Override
  public List<BookDTO> getTop100LoanedBooks(Category category) {
    return data4LibraryServiceApi.getPopularBooks(100, category.getKdc(), authKey, format)
        .response().docs().stream().map(doc -> doc.doc().toBook()).toList();
  }
}
