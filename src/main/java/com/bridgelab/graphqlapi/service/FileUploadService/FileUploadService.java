package com.bridgelab.graphqlapi.service.FileUploadService;

import com.bridgelab.graphqlapi.CustomResponse;
import graphql.schema.DataFetchingEnvironment;
import org.apache.catalina.core.ApplicationPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileUploadService {
    CustomResponse processFilesAsync(List<ApplicationPart> files);
}
