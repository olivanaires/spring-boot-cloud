package br.com.ota.udemy.discoveryservice.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLTypeField {
  private String name;
  private String description;
  private List<GraphQLArg> args;
  private GraphQLQueryFieldType type;
}
