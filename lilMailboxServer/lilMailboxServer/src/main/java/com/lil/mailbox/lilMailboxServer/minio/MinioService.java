package com.lil.mailbox.lilMailboxServer.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;

    final static String BUCKET_NAME = "lilmailbox";

    public String putContent(String content) {
        try {
            String objectName = UUID.randomUUID().toString();
            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(BUCKET_NAME).object(objectName + ".txt")
                            .stream(bais, bais.available(), -1)
                            .contentType("text/plain")
                            .build());
            return objectName;
        } catch (MinioException e) {
            System.err.println("Błąd podczas umieszczania obiektu: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        }
        return null;
    }

    public String getContent(String objectName) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName + ".txt")
                            .build());

            String content = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            stream.close();
            return content;
        } catch (MinioException e) {
            System.err.println("Błąd podczas pobierania obiektu: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        }
        return null;
    }
}
