package com.bookmile.backend.domain.image.dto.res;

import com.bookmile.backend.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageListResDto {
    private String imageUrls;

    public static ImageListResDto createImage(Image image) {
        return new ImageListResDto(
                image.getImageUrl()
        );
    }
}
