package com.bridgelab.graphqlapi.controller;

import com.bridgelab.graphqlapi.model.FileMetadata;
import com.bridgelab.graphqlapi.service.FillSearchService;
import jakarta.servlet.http.HttpServlet;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;




@RestController
@AllArgsConstructor
@RequestMapping("/uploads")
public class StaticFileController {
    private final FillSearchService fillSearchService;
    @GetMapping("/{fileName}")
    public ResponseEntity<?> getUpload(@PathVariable String fileName) throws IOException {
        FileMetadata fileMetadata = fillSearchService.getFileByName(fileName);
        if (fileMetadata == null) {
            return ResponseEntity.notFound().build();
        }
        Path filePath = Paths.get(fileMetadata.getUrl());
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(fileMetadata.getContentType()))
                .body(resource);
    }
}
