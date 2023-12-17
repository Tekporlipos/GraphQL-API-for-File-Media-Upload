package com.bridgelab.graphqlapi.repository;


import com.bridgelab.graphqlapi.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileMetaDataRepository extends JpaRepository<FileMetadata,UUID> {
    Optional<FileMetadata> findByFileName(String uuid);
}
