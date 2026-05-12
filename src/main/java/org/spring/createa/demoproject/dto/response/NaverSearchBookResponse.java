package org.spring.createa.demoproject.dto.response;

import java.util.List;

public record NaverSearchBookResponse(int total, List<Item> items) {

  record Item(String title, String image, String author, String publisher, String description,
              String isbn) {

  }
}
