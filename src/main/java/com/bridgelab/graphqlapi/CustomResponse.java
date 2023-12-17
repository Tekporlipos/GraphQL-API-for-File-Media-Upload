package com.bridgelab.graphqlapi;

import lombok.Data;

@Data
public class CustomResponse {
    private String status = "successful";
    private String message = "Request was processed successfully";
    private String data;
}
