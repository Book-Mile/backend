package com.bookmile.backend.domain.book.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailRequestDto {
    @NotBlank(message = "책의 ISBN13은 필수")
    private String isbn13;
}
