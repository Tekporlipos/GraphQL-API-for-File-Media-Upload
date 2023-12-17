package com.bridgelab.graphqlapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomMessage {
    private UUID sessionId;
    private byte[] message;
    private Map<String,String> metadata;
    private Date messageDate = new Date();

}
