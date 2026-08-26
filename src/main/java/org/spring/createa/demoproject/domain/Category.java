package org.spring.createa.demoproject.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public enum Category {
  TOTAL("전체", null),
  GENERAL_WORK("총류", "0"),
  PHILOSOPHY("철학", "1"),
  RELIGION("종교", "2"),
  SOCIAL_SCIENCE("사회과학", "3"),
  NATURAL_SCIENCE("자연과학", "4"),
  TECHNOLOGY("기술과학", "5"),
  ART("예술", "6"),
  LANGUAGE("언어", "7"),
  LITERATURE("문학", "8"),
  HISTORY("역사", "9");

  @Getter
  private final String value;
  @Getter
  private final String kdc;

  public static List<String> valuesList() {
    return Arrays.stream(values())
        .map(category -> category.value)
        .toList();
  }

  Category(String value, String kdc) {
    this.value = value;
    this.kdc = kdc;
  }

  public static Optional<Category> from(String value) {
    return Arrays.stream(values())
        .filter(c -> c.value.equals(value))
        .findFirst();
  }

}
