package org.spring.createa.demoproject.service;

import java.util.List;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;
import org.spring.createa.demoproject.dto.Library;

public interface ExternalBookService {

  List<BookDTO> getRecommendations(String isbn);

  List<Library> findOwnerLibraries(String isbn, String region, String dtlRegion);

  List<BookDTO> searchBooks(String query, Integer pageNo, Integer pageSize);

  BookDTO findBookByIsbn(String isbn);

  List<BookDTO> getTop100LoanedBooks(Category category);
}
