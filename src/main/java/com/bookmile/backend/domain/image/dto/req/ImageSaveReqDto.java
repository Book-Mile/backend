package com.bookmile.backend.domain.image.dto.req;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageSaveReqDto {
    @NotBlank(message = "Url 기입은 필수입니다.")
    private List<String> imageUrls;
}
