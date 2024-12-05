package com.bookmile.backend.domain.image.dto.req;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageSaveRequest {
    private List<String> imageUrls;
}
