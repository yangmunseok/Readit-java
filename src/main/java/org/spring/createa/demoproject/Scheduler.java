package org.spring.createa.demoproject;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.createa.demoproject.service.Data4LibraryService;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
  Data4LibraryService data4LibraryService;
  JobOperator jobOperator;
  Job saveDailyRanking;

  public Scheduler(Data4LibraryService data4LibraryService, JobOperator jobOperator,
      Job saveDailyRanking) {
    this.data4LibraryService = data4LibraryService;
    this.jobOperator = jobOperator;
    this.saveDailyRanking = saveDailyRanking;
  }

  @Scheduled(cron = "0 0 1 * * *")
  public void updateBookCache() {
    try {
      logger.info("update started");
      try {
        JobParameters params = new JobParametersBuilder()
            .addLocalDate("currentDate", LocalDate.now())
            .toJobParameters();
        jobOperator.start(saveDailyRanking, params);
        logger.info("Saved books");
      } catch (Exception e) {
        logger.error("Failed to save books", e);
      }
    } catch (Exception e) {
      logger.error("Unexpected error during updateBookCache", e);
    }
  }

}