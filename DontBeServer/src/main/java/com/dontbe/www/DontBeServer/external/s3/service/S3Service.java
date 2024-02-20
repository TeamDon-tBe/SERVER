package com.dontbe.www.DontBeServer.external.s3.service;

import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.dontbe.www.DontBeServer.external.s3.config.AWSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class S3Service {

    private static final String S3_URL = "https://dontbe-s3.s3.ap-northeast-2.amazonaws.com/";
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    private final String bucketName;
    private final AWSConfig awsConfig;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AWSConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }


    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        validateExtension(image);
        validateFileSize(image);

        final String key = "ProfileImage/" + directoryPath + "/" + generateImageFileName();
        final S3Client s3Client = awsConfig.getS3Client();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
        s3Client.putObject(request, requestBody);
        return S3_URL + key;
    }

    public void deleteImage(String key) {
        final S3Client s3Client = awsConfig.getS3Client();

        s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                builder.bucket(bucketName)
                        .key(key)
                        .build()
        );
    }


    private String generateImageFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new RuntimeException(ErrorStatus.UNVALID_PROFILEIMAGE_TYPE.getMessage());
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException(ErrorStatus.PROFILE_IMAGE_DATA_SIZE.getMessage());
        }
    }
}
