package org.spring.createa.demoproject.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PatchCommentRequestBody(int id, String content,
                                      @Min(value = 1, message = "별점은 최소 1점이어야 합니다.")
                                      @Max(value = 5, message = "별점은 최대 5점이어야 합니다.") Integer score) {

}