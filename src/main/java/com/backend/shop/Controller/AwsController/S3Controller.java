package com.backend.shop.Controller.AwsController;

import com.backend.shop.Service.AwsService.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    // Constructor injection for S3Service
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Handle file upload and store it in S3.
     * Returns the S3 object key and its URL.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty. Please select a valid file.");
        }

        // Upload file to S3 and get the generated key
        String key = s3Service.uploadFile(file);

        // Build file URL (if bucket/object is publicly accessible)
        String fileUrl = s3Service.getFileUrl(key);

        String responseMessage = "File uploaded successfully.\n" +
                "S3 key: " + key + "\n" +
                "URL: " + fileUrl;

        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Download file from S3 by object key.
     */
    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> download(@PathVariable String key) {
        // Download file bytes from S3
        byte[] data = s3Service.downloadFile(key);

        return ResponseEntity.ok()
                // Tell browser this is a downloadable attachment
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + key + "\"")
                // Set content type as binary stream
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(data);
    }
}
