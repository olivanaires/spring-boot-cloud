package br.com.ota.udemy.discoveryservice.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLMetadata {
  private List<GraphQLTypeMetadata> types;
}
