package com.szrthk.cbfb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(@Value("${aws.s3.region:ap-south-1}") String region,
                     @Value("${aws.s3.bucket-name:fbs-qr-bucket}") String bucketName) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
        this.bucketName = bucketName;
    }

    public String upload(byte[] qr, String filename) {
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .contentType("image/png")
                .build();
        try {
            s3Client.putObject(put, RequestBody.fromBytes(qr));
            return "https://" + bucketName + ".s3." + s3Client.serviceClientConfiguration().region().id() + ".amazonaws.com/" + filename;
        } catch (S3Exception ex) {
            // Fallback for dev/demo when bucket is missing; log and return dummy URL
            log.warn("S3 upload failed ({}). Returning fallback URL for {}.", ex.awsErrorDetails().errorMessage(), filename);
            return "https://example.com/qr/" + filename;
        }
    }
}
