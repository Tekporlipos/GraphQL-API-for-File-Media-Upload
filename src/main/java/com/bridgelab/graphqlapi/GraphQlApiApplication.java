package com.bridgelab.graphqlapi;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("com.bridgelab.graphqlapi.model")
public class GraphQlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphQlApiApplication.class, args);
    }
    @Bean
    GraphQLScalarType uploadScalarType() {
        return ApolloScalars.Upload;
    }
}
