package org.spring.createa.demoproject.dto;

public record Book(
    Integer no,
    String bookname,
    String authors,
    String publisher,
    String publication_date,
    String publication_year,
    String isbn,
    String isbn13,
    String addition_symbol,
    String vol,
    String class_no,
    String class_nm,
    String description,
    String bookImageURL
) {

}
