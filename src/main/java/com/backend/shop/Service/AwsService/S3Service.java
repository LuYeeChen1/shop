package com.backend.shop.Service.AwsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // Constructor injection for S3Client
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Upload file to S3 with a unique key to avoid overwriting.
     * Uses original filename, but prepends timestamp to ensure uniqueness.
     */
    public String uploadFile(MultipartFile file) throws IOException {

        String uniqueKey = "uploads/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueKey)
                .contentType(file.getContentType())  // Saves correct Content-Type in S3
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        return uniqueKey; // Return S3 key so controller can return URL
    }

    /**
     * Download a file from S3 by key.
     */
    public byte[] downloadFile(String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes =
                s3Client.getObjectAsBytes(getObjectRequest);

        return objectBytes.asByteArray();
    }

    /**
     * Build S3 public URL for object.
     * Only works if bucket/object is public or uses CloudFront/Presigned URL.
     */
    public String getFileUrl(String key) {
        return "https://" + bucketName + ".s3."
                + s3Client.serviceClientConfiguration().region().id()
                + ".amazonaws.com/" + key;
    }
}
