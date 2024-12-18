package com.bookmile.backend.domain.image.controller;

import static com.bookmile.backend.global.common.StatusCode.DELETE_IMAGE;
import static com.bookmile.backend.global.common.StatusCode.SAVE_IMAGE;
import static com.bookmile.backend.global.common.StatusCode.VIEW_IMAGE;

import com.bookmile.backend.domain.image.dto.req.ImageSaveReqDto;
import com.bookmile.backend.domain.image.dto.res.ImageListResDto;
import com.bookmile.backend.domain.image.service.ImageService;
import com.bookmile.backend.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private ImageService imageService;

    @GetMapping("{recordId}")
    public ResponseEntity<CommonResponse<List<ImageListResDto>>> viewImages(@PathVariable Long recordId) {
        List<ImageListResDto> images = imageService.viewImages(recordId);
        return ResponseEntity.status(VIEW_IMAGE.getStatus())
                .body(CommonResponse.from(VIEW_IMAGE.getMessage(), images));
    }

    @PostMapping
    public ResponseEntity<?> saveImages(@RequestParam Long recordId, @RequestBody ImageSaveReqDto imageSaveReqDto) {
        imageService.saveImages(recordId, imageSaveReqDto);
        return ResponseEntity.status(SAVE_IMAGE.getStatus())
                .body(CommonResponse.from(SAVE_IMAGE.getMessage(), null));
    }

    @DeleteMapping("{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.status(DELETE_IMAGE.getStatus())
                .body(CommonResponse.from(DELETE_IMAGE.getMessage(), null));
    }
}
