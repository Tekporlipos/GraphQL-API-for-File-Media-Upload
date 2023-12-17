package com.bridgelab.graphqlapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String fileName;
    private String message;
    @Column(unique = true)
    private String url;
    private long fileSize;
    private String contentType;
    private String fileExtension;

    @Override
    public String toString() {
        return "FileMetadata{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", contentType='" + contentType + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}
