package com.example.microservicio_examenes_complementarios.services;


import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.netflix.discovery.converters.Auto;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class    S3Service {

    @Autowired
    private ConverterVariablesService converterVariablesService;
    
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFileResultadoExamenComplementario(MultipartFile file) throws IOException {
        String fileName = converterVariablesService.arreglarFileName(file.getOriginalFilename());

        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        InputStream inputStream = file.getInputStream();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("resultados-examenes-complementarios/"+fileName)  
                .contentType(contentType)
                .build();

        try {
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.getSize()));

            return "resultados-examenes-complementarios/"+fileName;
        } catch (S3Exception e) {
            throw new IOException("Error al subir el archivo a S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
    public void deleteFile(String fileName) throws IOException {
    try {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteRequest);
    } catch (S3Exception e) {
        throw new IOException("Error al eliminar el archivo en S3: " + e.awsErrorDetails().errorMessage(), e);
    }
}
}
