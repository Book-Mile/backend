package com.bookmile.backend.domain.image.dto.res;

import com.bookmile.backend.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageListResponse {
    private String imageUrls;

    public static ImageListResponse createImage(Image image) {
        return new ImageListResponse(
                image.getImageUrl()
        );
    }
}
