package com.bookmile.backend.domain.image.service.Impl;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    List<String> viewImages(Long recordId);

    void saveImages(Long recordId, List<MultipartFile> files) throws IOException;

    void deleteImage(Long imageId);
}
