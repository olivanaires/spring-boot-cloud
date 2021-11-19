package br.com.ota.udemy.discoveryservice.service;

import br.com.ota.udemy.discoveryservice.model.catalog.*;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLArg;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLMetadataDataWrapper;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLTypeField;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLTypeMetadata;
import br.com.ota.udemy.discoveryservice.repository.ProductRepository;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CatalogService {

  private static final Object CATALOG_REQUEST_BODY =
      "{\"query\":\"{" +
              "  __schema {" +
              "    types {" +
              "      name" +
              "      description" +
              "      fields {" +
              "        name" +
              "        description" +
              "        args {" +
              "          name" +
              "          description" +
              "          type{" +
              "            kind" +
              "            name" +
              "            ofType {" +
              "              name" +
              "            }" +
              "          }" +
              "        }" +
              "        type {" +
              "          kind" +
              "          name" +
              "          ofType {" +
              "             name" +
              "          }" +
              "        }" +
              "      }" +
              "    }" +
              "  }" +
              "}" +
              "\",\"variables\":null}";

  private final RestTemplate restTemplate;
  private final ProductRepository productRepository;

  @Autowired
  public CatalogService(RestTemplate restTemplate, ProductRepository productRepository) {
    this.restTemplate = restTemplate;
    this.productRepository = productRepository;
  }

  private void fetchServiceCatalog(InstanceInfo instanceInfo) {
    String graphQlUrl = getGraphQlUrl(instanceInfo);
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("content-type", "application/json"); // maintain graphql

      ResponseEntity<GraphQLMetadataDataWrapper> objectResponseEntity = restTemplate.postForEntity(
              graphQlUrl,
              new HttpEntity<>(CATALOG_REQUEST_BODY, headers),
              GraphQLMetadataDataWrapper.class);

      GraphQLMetadataDataWrapper body = objectResponseEntity.getBody();
      List<GraphQLTypeMetadata> types = body.getData().getSchema().getTypes();
      Optional<GraphQLTypeMetadata> queryType = types.stream().filter(t -> t.getName().equals("Query")).findFirst();
      if (queryType.isPresent()) {
        Product product = mountProduct(instanceInfo, types, queryType.get());
        productRepository.save(product);
      }
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    }
  }

  private Product mountProduct(InstanceInfo instanceInfo, List<GraphQLTypeMetadata> types, GraphQLTypeMetadata queryType) {
    Product product = new Product();
    product.setName(instanceInfo.getAppName());

    Set<Query> productQueries = new HashSet<>();
    List<GraphQLTypeField> queries = queryType.getFields();
    queries.forEach(q -> {
      Query query = Query.builder()
              .product(product)
              .name(q.getName())
              .description(q.getDescription()).build();

      List<GraphQLArg> args = q.getArgs();
      if (!args.isEmpty()) {
        query.setParams(new HashSet<>());
        args.forEach(a -> {
          QueryArgField arqField = QueryArgField.builder()
                  .query(query)
                  .name(a.getName())
                  .type(a.getType().getName())
                  .description(a.getDescription()).build();
          query.getParams().add(arqField);
        });
      }

      String type = q.getType().getKind();
      String typeName = type.equals("LIST") ? q.getType().getOfType().getName() : q.getType().getName();
      QueryReturn queryReturn = QueryReturn.builder()
              .type(type)
              .name(typeName).build();

      Optional<GraphQLTypeMetadata> returnType = types.stream().filter(t -> t.getName().equals(typeName)).findFirst();
      if (returnType.isPresent() && !returnType.get().getFields().isEmpty()) {
        queryReturn.setFields(new HashSet<>());
        returnType.get().getFields().forEach(f -> {
          QueryReturnField returnField = QueryReturnField.builder()
                  .queryReturn(queryReturn)
                  .name(f.getName())
                  .type(f.getType().getName())
                  .description(f.getDescription()).build();
          queryReturn.getFields().add(returnField);
        });
      }
      query.setQueryReturn(queryReturn);
      productQueries.add(query);
    });
    product.setQueries(productQueries);
    return product;
  }

  private String getGraphQlUrl(InstanceInfo instanceInfo) {
    return instanceInfo.getHomePageUrl() + "/graphql";
  }

  public void updateCatalog(InstanceInfo instInfo) {
    fetchServiceCatalog(instInfo);
  }
}
