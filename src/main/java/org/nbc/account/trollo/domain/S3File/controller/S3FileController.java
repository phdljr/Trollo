package org.nbc.account.trollo.domain.S3File.controller;

import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.S3File.entity.S3File;
import org.nbc.account.trollo.domain.S3File.repository.S3FileRepository;
import org.nbc.account.trollo.domain.S3File.service.S3FileService;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.card.repository.CardRepository;
import org.nbc.account.trollo.domain.comment.exception.CommentDomainException;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.nbc.account.trollo.global.util.S3Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3FileController {

    private final AmazonS3 amazonS3;
    private final S3FileService s3FileService;
    private final S3Provider s3Provider;
    private final CardRepository cardRepository;
    private final S3FileRepository s3FileRepository;
    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;

    @PostMapping("/api/cards/{cardId}/files") // 마지막에 다 수정 예정
    public ApiResponse<Void> saveFile(@PathVariable Long cardId, MultipartFile multipartFile)
        throws IOException {
        s3FileService.saveFile(cardId, multipartFile);
        return new ApiResponse<>(HttpStatus.OK.value(), "save_File");
    }

    @PutMapping("/api/cards/{cardId}/files/{fileId}")
    public ApiResponse<Void> updateFile(
        @PathVariable Long cardId,
        @PathVariable Long fileId,
        MultipartFile multipartFile)
        throws IOException {
        s3FileService.updateFile(cardId, fileId, multipartFile);
        return new ApiResponse<>(HttpStatus.OK.value(), "update_File");
    }

    @DeleteMapping("/api/cards/files/{fileId}")
    public ApiResponse<Void> deleteFile(@PathVariable Long fileId) {
        s3FileService.deletFile(fileId);
        return new ApiResponse<>(HttpStatus.OK.value(), "delete_File");
    }

    @GetMapping("/api/cards/{cardId}/files/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long cardId,
        @PathVariable Long fileId) {
        Card card = cardRepository.findCardById(cardId);
        if (card == null) {
            throw new CommentDomainException(ErrorCode.NOT_FOUND_CARD);
        }
        S3File s3File = s3FileRepository.findS3FileById(fileId);
        byte[] data = s3Provider.downloadFile(s3File.getFileName());
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
            .ok()
            .contentLength(data.length)
            .header("Content-type", "application/octet-stream")
            .header("Content-disposition", "attachment; filename=\""
                + s3File.getFilePath()
                + s3File.getFileName()
                + "\"")
            .body(resource);
    }

}