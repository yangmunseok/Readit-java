package org.spring.createa.demoproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.createa.demoproject.dto.response.PopularBookResponse;
import org.spring.createa.demoproject.service.Data4LibraryServiceAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

  Data4LibraryServiceAdapter data4LibraryServiceAdapter;

  public Scheduler(Data4LibraryServiceAdapter data4LibraryServiceAdapter) {
    this.data4LibraryServiceAdapter = data4LibraryServiceAdapter;
  }

  @Scheduled(fixedRate = 3600000 * 24)
  public void updateBookCache() {
    try {
      logger.info("Cache update started");
      for (int i = 1; i < 10; i++) {
        try {
          PopularBookResponse response = data4LibraryServiceAdapter.cachingGetPopularBooks(null,
              Integer.toString(i));
          logger.info("Cached books for category: {}, response: {}", i, response);
        } catch (Exception e) {
          logger.error("Failed to cache books for category: {}", i, e);
        }
      }

      try {
        PopularBookResponse response = data4LibraryServiceAdapter.cachingGetPopularBooks(null,
            null);
        logger.info("Cached all popular books, response: {}", response);
      } catch (Exception e) {
        logger.error("Failed to cache all popular books", e);
      }

      logger.info("Cache update completed");
    } catch (Exception e) {
      logger.error("Unexpected error during cache update", e);
    }
  }
}