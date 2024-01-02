package org.nbc.account.trollo.global.util;

import com.amazonaws.services.s3.AmazonS3;

public class S3Validator {

    public static void validate(AmazonS3 amazonS3, String bucket, String fileName) {
        if (!isExistFile(amazonS3, bucket, fileName)) {
            throw new IllegalArgumentException("Not Found File");
        }
    }

    private static boolean isExistFile(AmazonS3 amazonS3, String bucket, String filename) {
        return amazonS3.doesObjectExist(bucket, filename);
    }
}
