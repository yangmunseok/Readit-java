package org.spring.createa.demoproject.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spring.createa.demoproject.domain.BookRanking;
import org.spring.createa.demoproject.domain.QBookRanking;

@Slf4j
public class BookRankingRepositoryUsingQuerydslImpl implements BookRankingRepositoryUsingQuerydsl {

  JPAQueryFactory queryFactory;
  QBookRanking rank = QBookRanking.bookRanking;

  public BookRankingRepositoryUsingQuerydslImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public List<BookRanking> getPopularBooks(int size, BookRanking.Category category) {
    LocalDate today = LocalDate.now();

    List<BookRanking> bookRankings = queryFactory
        .selectFrom(rank)
        .orderBy(rank.ranking.asc())
        .limit(size)
        .where(rank.createdAt.eq(today).and(rank.category.eq(category)))
        .fetch();

    if (bookRankings.isEmpty()) {
      LocalDate yesterday = LocalDate.now().minusDays(1);
      List<BookRanking> yesterdayBookRankings = queryFactory
          .selectFrom(rank)
          .orderBy(rank.ranking.asc())
          .limit(size)
          .where(rank.createdAt.eq(yesterday).and(rank.category.eq(category)))
          .fetch();

      if (yesterdayBookRankings.isEmpty()) {
        log.error("책[{}] 랭킹정보가 존재하지 않습니다.", category);
      }
      return yesterdayBookRankings;
    }

    return bookRankings;
  }
}
