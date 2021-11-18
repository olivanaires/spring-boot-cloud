package br.com.ota.udemy.discoveryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.Duration;

@Service
public class CatalogService {

  private static final Object CATALOG_REQUEST_BODY =
      "{\"query\":\"{\\n  __schema {\\n    queryType {\\n      fields {\\n        name\\n        description\\n        args {\\n          name\\n          description\\n          type {\\n            name\\n          }\\n        }\\n        type {\\n          name\\n        }\\n      }\\n    }\\n    types {\\n      name\\n      description\\n      fields {\\n        name\\n        description\\n        type {\\n          name\\n        }\\n      }\\n    }\\n  }\\n}\\n\",\"variables\":null}";
  @Autowired private RestTemplate restTemplate;

  @Autowired private Clock clock;

  @Value("${relatorio.catalog.refresh-interval:1m}")
  private Duration refreshInterval;

  private void fetchServiceCatalog(InstanceInfo instanceInfo) {
    String graphQlUrl = getGraphQlUrl(instanceInfo);
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("content-type", "application/json"); // maintain graphql


      ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(
              graphQlUrl,
              new HttpEntity<>(CATALOG_REQUEST_BODY, headers),
              Object.class);
      Object body = objectResponseEntity.getBody();
      ObjectMapper mapper = new ObjectMapper();
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  private String getGraphQlUrl(InstanceInfo instanceInfo) {
    return "http://localhost:8081/graphql";
  }

  public void updateCatalog(InstanceInfo instInfo) {
    String applicationName = instInfo.getAppName();
    fetchServiceCatalog(instInfo);
  }
}
