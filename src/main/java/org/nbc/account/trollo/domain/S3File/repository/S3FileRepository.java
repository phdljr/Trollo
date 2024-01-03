package org.nbc.account.trollo.domain.S3File.repository;

import org.nbc.account.trollo.domain.S3File.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRepository extends JpaRepository<S3File, Long> {

    S3File findS3FileById(Long fileId);
}