package com.bookmile.backend.domain.image.service.Impl;

import static com.bookmile.backend.global.common.StatusCode.IMAGE_NOT_FOUND;
import static com.bookmile.backend.global.common.StatusCode.RECORD_NOT_FOUND;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bookmile.backend.domain.image.entity.Image;
import com.bookmile.backend.domain.image.repository.ImageRepository;
import com.bookmile.backend.domain.record.entity.Record;
import com.bookmile.backend.domain.record.repository.RecordRepository;
import com.bookmile.backend.global.exception.CustomException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {


    // s3 버킷 클라이언트
    private final AmazonS3Client s3Client;
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
    public void saveImages(Long recordId, List<MultipartFile> multipartFiles) throws IOException {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(RECORD_NOT_FOUND));
        String bucketName = "bookmile";

        List<String> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            files.add(uploadFileToS3Bucket(bucketName, file));
        }

        List<Image> images = files.stream()
                .map(url -> new Image(record, url))
                .toList();

        imageRepository.saveAll(images);


    }


    // S3에 업로드 하기
    public String uploadFileToS3Bucket(String bucketName, MultipartFile multiPartFile) throws IOException {
        File file = convertMultiPartToFile(multiPartFile);
        String fileName = System.currentTimeMillis() + "_" + multiPartFile.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete(); // 임시 파일 삭제

        return s3Client.getResourceUrl(bucketName, fileName); // s3에 저장된 파일의 Url 받기
    }

    // MultiPart 를 파일로 변환 (임시 파일 생성)
    private File convertMultiPartToFile(MultipartFile multiPartFile) throws IOException {
        File convFile = new File(multiPartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);

        fos.write(multiPartFile.getBytes());
        fos.close();

        return convFile;
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));

        image.delete(image);

        imageRepository.save(image);
    }
}
