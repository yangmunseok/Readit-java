package org.spring.createa.demoproject.Repository;

import java.time.LocalDate;
import org.spring.createa.demoproject.domain.BookRanking;
import org.spring.createa.demoproject.domain.BookRanking.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRankingRepository extends JpaRepository<BookRanking, Integer>,
    BookRankingRepositoryUsingQuerydsl {

  Integer deleteAllByCategoryAndCreatedAtBefore(Category category, LocalDate createdAtBefore);
}

//설계부터 하자.
//배치 쓰는게 좋을 것 같다. 왜냐하면 실패하는 경우 상태 추적을 일일히 구현하는 것보다,
// 배치 써서 하는 것이 나을 것 같아서이다.
