package br.com.ota.udemy.discoveryservice.model.graphql;

import lombok.Data;

@Data
public class GraphQLArg {
    private String name;
    private String description;
    private GraphQLQueryFieldType type;
}
