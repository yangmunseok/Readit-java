package org.spring.createa.demoproject.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.spring.createa.demoproject.domain.Category;
import org.spring.createa.demoproject.dto.BookDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class CSVBookRankingProvider implements BookRankingProvider {


  private static final String DEFAULT_BOOK_IMAGE_URL =
      "https://image.aladin.co.kr/product/24512/70/cover/k392630952_1.jpg";

  @Override
  public List<BookDTO> getTop100LoanedBooks(Category category) {
    String resourcePath = "csv/BestLoanList_" + category.getValue() + ".csv";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IllegalArgumentException("CSV resource not found: " + resourcePath);
    }

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      return readBooks(reader);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read CSV resource: " + resourcePath, e);
    }
  }

  private List<BookDTO> readBooks(BufferedReader reader) throws IOException {
    List<BookDTO> books = new ArrayList<>();
    boolean dataSection = false;
    String line;
    while ((line = reader.readLine()) != null) {
      List<String> columns = parseCsvLine(line);
      if (!dataSection) {
        dataSection = isHeader(columns);
        continue;
      }
      if (columns.size() < 9 || columns.get(0).isBlank()) {
        continue;
      }
      books.add(new BookDTO(
          null, columns.get(1), columns.get(2), columns.get(3),
          null, columns.get(4), columns.get(6), columns.get(7), columns.get(5), columns.get(8),
          null, null, DEFAULT_BOOK_IMAGE_URL, Integer.parseInt(columns.get(0))));
    }
    return books;
  }

  private boolean isHeader(List<String> columns) {
    return columns.size() >= 9 && ("순위".equals(columns.get(0)) || "ranking".equals(columns.get(0)));
  }

  private List<String> parseCsvLine(String line) {
    List<String> columns = new ArrayList<>();
    StringBuilder value = new StringBuilder();
    boolean quoted = false;
    for (int i = 0; i < line.length(); i++) {
      char character = line.charAt(i);
      if (character == '"') {
        if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          value.append(character);
          i++;
        } else {
          quoted = !quoted;
        }
      } else if (character == ',' && !quoted) {
        columns.add(value.toString());
        value.setLength(0);
      } else {
        value.append(character);
      }
    }
    columns.add(value.toString());
    return columns;
  }
}
