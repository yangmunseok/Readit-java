package org.spring.createa.demoproject;

import lombok.extern.slf4j.Slf4j;
import org.spring.createa.demoproject.service.BookRankingService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchListener {

  BookRankingService bookRankingService;

  public BatchListener(BookRankingService bookRankingService) {
    this.bookRankingService = bookRankingService;
  }

  @AfterJob
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("배치 실행 완료되었습니다.");
      bookRankingService.refreshBookCache();
    }
  }
}
