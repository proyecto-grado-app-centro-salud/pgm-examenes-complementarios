package com.example.microservicio_examenes_complementarios.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConverterVariablesService {
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    public String convertirKeyImagenAEnlaceImagen(String key){
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
    public String arreglarFileName(String fileName){
        return System.currentTimeMillis() + "_" + fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
