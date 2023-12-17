package com.bridgelab.graphqlapi.controller;

import com.bridgelab.graphqlapi.model.FileMetadata;
import com.bridgelab.graphqlapi.service.FillSearchService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@AllArgsConstructor
public class QueryResolver implements GraphQLQueryResolver {

    private final FillSearchService fillSearchService;
    public Page<FileMetadata> getAllFiles(int page, int pageSize) {
        return fillSearchService.getAllFilesMeta(PageRequest.of(page, pageSize != 0? pageSize :10));
    }

    public FileMetadata getFile(UUID uuid) {
        return fillSearchService.getFileMeta(uuid);
    }
}
