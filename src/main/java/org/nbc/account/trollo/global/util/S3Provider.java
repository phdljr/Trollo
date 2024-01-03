package org.nbc.account.trollo.global.util;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
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

    public String saveFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        // S3에 저장 시 파일이름은 UUID와 파일명을 합하여 저장해야합니다.
        // 1. 파일명이 중복될수도 있디.
        // 2. 보안적인 이유
        originalFilename = UUID.randomUUID() + originalFilename.substring(
            originalFilename.lastIndexOf("."));

        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public void deleteImage(String originalFilename) {
        if (originalFilename == null) {
            return;
        }
        amazonS3.deleteObject(bucket, originalFilename);
    }

    public String updateImage(String originalFilename, MultipartFile multipartFile)
        throws IOException {
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public byte[] downloadFile(String originalFilename) {
        System.out.println("originalFilename = " + originalFilename);
        S3Object s3Object = amazonS3.getObject(bucket, originalFilename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}