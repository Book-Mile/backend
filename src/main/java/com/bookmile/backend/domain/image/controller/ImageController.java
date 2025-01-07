package com.bookmile.backend.domain.image.controller;

import static com.bookmile.backend.global.common.StatusCode.DELETE_IMAGE;
import static com.bookmile.backend.global.common.StatusCode.SAVE_IMAGE;
import static com.bookmile.backend.global.common.StatusCode.VIEW_IMAGE;

import com.bookmile.backend.domain.image.service.Impl.ImageServiceImpl;
import com.bookmile.backend.global.common.CommonResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceImpl imageServiceImpl;

    @GetMapping("{recordId}")
    public ResponseEntity<CommonResponse<List<String>>> viewImages(@PathVariable Long recordId) {
        List<String> images = imageServiceImpl.viewImages(recordId);
        return ResponseEntity.status(VIEW_IMAGE.getStatus())
                .body(CommonResponse.from(VIEW_IMAGE.getMessage(), images));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveImages(@RequestParam Long recordId,
                                        @RequestParam("files") List<MultipartFile> files) throws IOException {
        imageServiceImpl.saveImages(recordId, files);
        return ResponseEntity.status(SAVE_IMAGE.getStatus())
                .body(CommonResponse.from(SAVE_IMAGE.getMessage(), null));
    }

    @DeleteMapping("{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        imageServiceImpl.deleteImage(imageId);
        return ResponseEntity.status(DELETE_IMAGE.getStatus())
                .body(CommonResponse.from(DELETE_IMAGE.getMessage(), null));
    }
}
