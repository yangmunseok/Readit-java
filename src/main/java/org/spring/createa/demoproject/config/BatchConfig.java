package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.BatchListener;
import org.spring.createa.demoproject.service.BookRankingService;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final BookRankingService bookRankingService;
  private final BatchListener batchListener;
  private final RetryTemplate retryTemplate =
      RetryTemplate.builder()
          .maxAttempts(3)
          .exponentialBackoff(1000, 2.0, 10000)
          .build();

  public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      BookRankingService bookRankingService, BatchListener batchListener) {
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
    this.bookRankingService = bookRankingService;
    this.batchListener = batchListener;
  }

  @Bean
  Job saveDailyRanking() {
    return new JobBuilder("saveDailyRanking", jobRepository)
        .start(saveDailyOverallRanking())
        .next(saveDailyCategoryRanking("0"))
        .next(saveDailyCategoryRanking("1"))
        .next(saveDailyCategoryRanking("2"))
        .next(saveDailyCategoryRanking("3"))
        .next(saveDailyCategoryRanking("4"))
        .next(saveDailyCategoryRanking("5"))
        .next(saveDailyCategoryRanking("6"))
        .next(saveDailyCategoryRanking("7"))
        .next(saveDailyCategoryRanking("8"))
        .next(saveDailyCategoryRanking("9"))
        .listener(batchListener)
        .build();
  }

  Step saveDailyCategoryRanking(String kdc) {
    return new StepBuilder("saveDailyCategoryRanking" + kdc, jobRepository).tasklet(
        (contribution, chunkContext) -> {
          retryTemplate.execute((retryContext -> {
            bookRankingService.saveCategoryRanking(kdc);
            return null;
          }));
          return RepeatStatus.FINISHED;
        }, transactionManager).build();
  }

  Step saveDailyOverallRanking() {
    return new StepBuilder("saveDailyOverallRanking", jobRepository).tasklet(
        (contribution, chunkContext) -> {
          retryTemplate.execute((retryContext -> {
            bookRankingService.saveOverallRanking();
            return null;
          }));

          return RepeatStatus.FINISHED;
        }, transactionManager).build();
  }
  
}

