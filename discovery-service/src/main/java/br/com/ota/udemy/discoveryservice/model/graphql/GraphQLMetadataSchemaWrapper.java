package br.com.ota.udemy.discoveryservice.model.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GraphQLMetadataSchemaWrapper {
  @JsonProperty("__schema")
  private GraphQLMetadata schema;
}
