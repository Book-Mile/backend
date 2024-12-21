package com.bookmile.backend.domain.image.service.Impl;

import com.bookmile.backend.domain.image.dto.req.ImageSaveReqDto;
import com.bookmile.backend.domain.image.dto.res.ImageListResDto;
import java.util.List;

public interface ImageService {
    List<ImageListResDto> viewImages(Long recordId);

    void saveImages(Long recordId, ImageSaveReqDto imageSaveReqDto);

    void deleteImage(Long imageId);
}
