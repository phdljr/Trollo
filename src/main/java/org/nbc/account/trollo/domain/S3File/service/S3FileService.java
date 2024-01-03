package org.nbc.account.trollo.domain.S3File.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {

    void saveFile(Long cardId, MultipartFile multipartFile) throws IOException;

    void updateFile(Long cardId, Long fileId, MultipartFile multipartFile) throws IOException;

    void deletFile(Long fileId);

}