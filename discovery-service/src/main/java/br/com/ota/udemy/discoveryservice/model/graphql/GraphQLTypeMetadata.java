package br.com.ota.udemy.discoveryservice.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLTypeMetadata {
  private String name;
  private String description;
  private List<GraphQLTypeField> fields;
}
