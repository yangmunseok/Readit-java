package org.spring.createa.demoproject.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PostCommentRequestBody(String isbn13, @NotBlank String comment,
                                     @Min(value = 1, message = "별점은 최소 1점이어야 합니다.")
                                     @Max(value = 5, message = "별점은 최대 5점이어야 합니다.") Integer score) {

}
