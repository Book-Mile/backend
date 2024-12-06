package com.bookmile.backend.domain.image.service;

import com.bookmile.backend.domain.image.dto.req.ImageSaveReqDto;
import com.bookmile.backend.domain.image.dto.res.ImageListResDto;
import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.image.repository.ImageRepository;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final RecordRepository recordRepository;

    public List<ImageListResDto> viewImages(Long recordId) {
        recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("없는 recordId 입니다."));

        return imageRepository.findAllByRecordId(recordId)
                .stream()
                .map(ImageListResDto::createImage)
                .toList();
    }

    public void saveImages(Long recordId, ImageSaveReqDto imageSaveReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("없는 기록 ID 입니다."));

        List<Image> images = imageSaveReqDto.getImageUrls().stream()
                .map(url -> new Image(record, url))
                .toList();

        imageRepository.saveAll(images);
    }

    public Long deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("없는 이미지 입니다."));

        image.delete(image);

        imageRepository.save(image);

        return image.getId();
    }
}
