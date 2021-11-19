package br.com.ota.udemy.discoveryservice.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLQueryFieldType {
  private String kind;
  private String name;
  private GraphQLType ofType;
}
