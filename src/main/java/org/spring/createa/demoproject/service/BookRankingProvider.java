package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;

public interface BookRankingProvider {

  List<BookDTO> getTop100LoanedBooks(Category category);
}
