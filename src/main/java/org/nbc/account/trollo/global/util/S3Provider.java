package org.nbc.account.trollo.global.util;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Provider {

    private final AmazonS3 amazonS3;
    private final String SEPARATOR = "/";
    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;

    // S3에 파일을 업로드 하기 위해서는 ObjectMetaData 형태로 반환
    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    private void createFolder(String folderName) {
        if (!amazonS3.doesObjectExist(bucket, folderName)) {
            amazonS3.putObject(
                bucket,
                folderName + SEPARATOR,
                new ByteArrayInputStream(new byte[0]),
                new ObjectMetadata());
        }
    }

    public String saveFile(MultipartFile multipartFile, String folderName) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        // S3에 저장 시 파일이름은 UUID와 파일명을 합하여 저장해야합니다.
        // 1. 파일명이 중복될수도 있디.
        // 2. 보안적인 이유
        originalFilename = folderName + SEPARATOR + UUID.randomUUID() + originalFilename.substring(
            originalFilename.lastIndexOf("."));
        createFolder(folderName);

        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public String updateImage(String originalFilename, MultipartFile multipartFile)
        throws IOException {
        S3Validator.validate(amazonS3, bucket, originalFilename);
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public void deleteImage(String originalFilename) {
        if (originalFilename == null) {
            return;
        }
        S3Validator.validate(amazonS3, bucket, originalFilename);
        amazonS3.deleteObject(bucket, originalFilename);
    }

}
