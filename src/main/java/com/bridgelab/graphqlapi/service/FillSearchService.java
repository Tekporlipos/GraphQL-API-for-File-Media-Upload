package com.bridgelab.graphqlapi.service;


import com.bridgelab.graphqlapi.model.FileMetadata;
import com.bridgelab.graphqlapi.repository.FileMetaDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FillSearchService {
    private final FileMetaDataRepository fileMetaDataRepository;


    public FileMetadata getFileMeta(UUID uuid){
        return fileMetaDataRepository.findById(uuid).orElseThrow();
    }

    public FileMetadata getFileByName(String getFileByName){
        return fileMetaDataRepository.findByFileName(getFileByName).orElseThrow();
    }

    public Page<FileMetadata> getAllFilesMeta(Pageable pageable){
        return fileMetaDataRepository.findAll(pageable);
    }
}
