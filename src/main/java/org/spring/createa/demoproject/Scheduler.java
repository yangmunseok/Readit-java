package org.spring.createa.demoproject;

import org.spring.createa.demoproject.service.Data4LibraryServiceAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  Data4LibraryServiceAdapter data4LibraryServiceAdapter;

  public Scheduler(Data4LibraryServiceAdapter data4LibraryServiceAdapter) {
    this.data4LibraryServiceAdapter = data4LibraryServiceAdapter;
  }

  @Scheduled(fixedRate = 3600000 * 24)
  public void updateBookCache() {
    for (int i = 1; i < 10; i++) {
      data4LibraryServiceAdapter.cachingGetPopularBooks(null, Integer.toString(i));
    }
    data4LibraryServiceAdapter.cachingGetPopularBooks(null, null);
  }
}
