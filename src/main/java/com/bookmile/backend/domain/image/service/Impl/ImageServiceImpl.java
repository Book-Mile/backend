package com.bookmile.backend.domain.image.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.IMAGE_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.RECORD_NOT_FOUND;

import com.bookmile.backend.domain.image.dto.req.ImageSaveReqDto;
import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.image.repository.ImageRepository;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final RecordRepository recordRepository;

    @Override
    public List<String> viewImages(Long recordId) {
        recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(RECORD_NOT_FOUND));

        return imageRepository.findAllByRecordId(recordId)
                .stream()
                .map(Image::getImageUrl)
                .toList();
    }

    @Override
    public void saveImages(Long recordId, ImageSaveReqDto imageSaveReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(RECORD_NOT_FOUND));

        List<Image> images = imageSaveReqDto.getImageUrls().stream()
                .map(url -> new Image(record, url))
                .toList();

        imageRepository.saveAll(images);
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));

        image.delete(image);

        imageRepository.save(image);
    }
}
