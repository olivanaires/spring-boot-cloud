package br.com.ota.udemy.discoveryservice.service;

import br.com.ota.udemy.discoveryservice.model.catalog.QueryReturn;
import br.com.ota.udemy.discoveryservice.model.catalog.Query;
import br.com.ota.udemy.discoveryservice.model.catalog.QueryField;
import br.com.ota.udemy.discoveryservice.model.catalog.Product;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLArg;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLMetadataDataWrapper;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLTypeField;
import br.com.ota.udemy.discoveryservice.model.graphql.GraphQLTypeMetadata;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
  @Autowired private RestTemplate restTemplate;

  @Autowired private Clock clock;

  @Value("${relatorio.catalog.refresh-interval:1m}")
  private Duration refreshInterval;

  private void fetchServiceCatalog(InstanceInfo instanceInfo) {

    String graphQlUrl = getGraphQlUrl(instanceInfo);
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("content-type", "application/json"); // maintain graphql

      Product product = new Product();
      product.setName(instanceInfo.getAppName());
      product.setQueries(new ArrayList<>());
      ResponseEntity<GraphQLMetadataDataWrapper> objectResponseEntity = restTemplate.postForEntity(
              graphQlUrl,
              new HttpEntity<>(CATALOG_REQUEST_BODY, headers),
              GraphQLMetadataDataWrapper.class);

      GraphQLMetadataDataWrapper body = objectResponseEntity.getBody();
      List<GraphQLTypeMetadata> types = body.getData().getSchema().getTypes();
      Optional<GraphQLTypeMetadata> queryType = types.stream().filter(t -> t.getName().equals("Query")).findFirst();
      if (queryType.isPresent()) {
        mountProduct(product, types, queryType);
      }
      System.out.println(product);
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    }
  }

  private void mountProduct(Product product, List<GraphQLTypeMetadata> types, Optional<GraphQLTypeMetadata> queryType) {
    List<GraphQLTypeField> queries = queryType.get().getFields();
    queries.forEach(q -> {
      Query query = new Query();
      query.setName(q.getName());
      query.setDescription(q.getDescription());
      List<GraphQLArg> args = q.getArgs();
      if (!args.isEmpty()) {
        query.setParams(new ArrayList<>());
        args.forEach(a -> {
          QueryField arg = new QueryField();
          arg.setName(a.getName());
          arg.setType(a.getType().getName());
          arg.setDescription(a.getDescription());
          query.getParams().add(arg);
        });
      }

      QueryReturn returnType = new QueryReturn();
      String type = q.getType().getKind();
      returnType.setType(type);

      String typeName = type.equals("LIST") ? q.getType().getOfType().getName() : q.getType().getName();
      returnType.setName(typeName);

      Optional<GraphQLTypeMetadata> queryReturn = types.stream().filter(t -> t.getName().equals(typeName)).findFirst();
      if (queryReturn.isPresent() && !queryReturn.get().getFields().isEmpty()) {
        returnType.setFields(new ArrayList<>());
        queryReturn.get().getFields().forEach(f -> {
          QueryField arg = new QueryField();
          arg.setName(f.getName());
          arg.setType(f.getType().getName());
          arg.setDescription(f.getDescription());
          returnType.getFields().add(arg);
        });
      }

      query.setReturnType(returnType);
      product.getQueries().add(query);
    });
  }

  private String getGraphQlUrl(InstanceInfo instanceInfo) {
    return instanceInfo.getHomePageUrl() + "/graphql";
  }

  public void updateCatalog(InstanceInfo instInfo) {
    fetchServiceCatalog(instInfo);
  }
}
