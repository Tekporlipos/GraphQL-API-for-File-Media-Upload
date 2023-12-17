package com.bridgelab.graphqlapi.controller;

import com.bridgelab.graphqlapi.CustomResponse;
import com.bridgelab.graphqlapi.service.FileUploadService.FileUploadService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.apache.catalina.core.ApplicationPart;

import java.util.List;


@Component
@AllArgsConstructor
public class FileManagerController implements GraphQLMutationResolver {
    private final FileUploadService fileUploadService;

    public CustomResponse multipleUpload(List<ApplicationPart> files) {
        return fileUploadService.processFilesAsync(files);
    }
}