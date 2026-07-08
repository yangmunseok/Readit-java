package org.spring.createa.demoproject.Repository;

import java.util.List;
import org.spring.createa.demoproject.domain.BookRanking;

public interface BookRankingRepositoryUsingQuerydsl {

  public List<BookRanking> getPopularBooks(int size, BookRanking.Category category);
}
