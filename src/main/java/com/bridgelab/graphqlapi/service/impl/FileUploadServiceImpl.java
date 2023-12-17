package com.bridgelab.graphqlapi.service.impl;

import com.bridgelab.graphqlapi.CustomMessage;
import com.bridgelab.graphqlapi.CustomResponse;
import com.bridgelab.graphqlapi.config.MQConfig;
import com.bridgelab.graphqlapi.service.FileUploadService.FileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationPart;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public CustomResponse processFilesAsync(List<ApplicationPart> files){
        if (files != null){
            UUID sessionId = UUID.randomUUID();
            for (ApplicationPart file : files) {
                enqueueFileUpload(file,sessionId);
            }
            return objectMapper.convertValue(Map.of("status","Success", "Message","Use the id to listen to the status of your upload(s)","data",sessionId.toString() ), CustomResponse.class);
        }
        return objectMapper.convertValue(Map.of("status","Failed", "Message","Bad request","data",null ), CustomResponse.class);
    }

    private void enqueueFileUpload(ApplicationPart file,UUID sessionId) {
        try {
            CustomMessage customMessage = new CustomMessage();
            customMessage.setMessage(file.getInputStream().readAllBytes());
            customMessage.setSessionId(sessionId);
            customMessage.setMetadata(extractMetadata(file));
            amqpTemplate.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, customMessage);
        }catch (Exception e){
            e.getStackTrace();
        }

    }

    public Map<String, String> extractMetadata(ApplicationPart file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("fileName", file.getSubmittedFileName());
        metadata.put("fileSize", String.valueOf(file.getSize()));
        metadata.put("contentType", file.getContentType());
        metadata.put("fileExtension", getFileExtension(file.getSubmittedFileName()));
        return metadata;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename != null) {
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex != -1) {
                return originalFilename.substring(lastDotIndex + 1);
            }
        }
        return "";
    }
}
