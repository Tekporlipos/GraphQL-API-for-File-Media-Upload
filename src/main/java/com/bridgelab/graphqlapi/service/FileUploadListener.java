package com.bridgelab.graphqlapi.service;

import com.bridgelab.graphqlapi.CustomMessage;
import com.bridgelab.graphqlapi.model.FileMetadata;
import com.bridgelab.graphqlapi.repository.FileMetaDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class FileUploadListener {

    private final FileMetaDataRepository fileMetaDataRepository;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;


    @RabbitListener(queues = "${file.upload.queue}")
    public void processFileUpload(CustomMessage message) {
        uploadFileToAbsolutePath(message);
    }

    public void uploadFileToAbsolutePath(CustomMessage message) {
        try {
            Path directory = Paths.get("uploads");
            String fileName = message.getMetadata().get("fileName");
            Files.createDirectories(directory);
            Path filePath = Paths.get(directory.toString(), fileName);
            Files.write(filePath, message.getMessage());
            saveMetadata(message, filePath.toString());
        } catch (IOException e) {
            messagingTemplate.convertAndSend("/topic/" + message.getSessionId(),
                    Map.of("message", e.getMessage(), "data", e.getStackTrace(), "status", "failed"));
        }
    }

    public void uploadFileToS3(CustomMessage message, String key, String bucketName, String simulatedPath) {
        try (S3Client s3Client = S3Client.builder().region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            String fullKey = simulatedPath + key;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullKey)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(message.getMessage()));
            saveMetadata(message, fullKey);
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/"+message.getSessionId(), Map.of("message",e.getMessage(),"data",e.getStackTrace(),"status","failed"));
        }
    }

    private void saveMetadata(CustomMessage message,String path){

        FileMetadata metadataEntry = objectMapper.convertValue(message.getMetadata(), FileMetadata.class);
        try {
            metadataEntry.setUrl(path);
            metadataEntry.setMessage("File saved successful to disk");
            FileMetadata save = fileMetaDataRepository.save(metadataEntry);
            messagingTemplate.convertAndSend("/topic/"+message.getSessionId(), save);
        }catch (Exception e){
            metadataEntry.setMessage("File already exist and it has been overwrite on disk");
            messagingTemplate.convertAndSend("/topic/"+message.getSessionId(), metadataEntry);
            e.getStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
