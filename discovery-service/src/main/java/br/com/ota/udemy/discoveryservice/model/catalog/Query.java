package br.com.ota.udemy.discoveryservice.model.catalog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Query {
    private String name;
    private String description;
    private List<QueryField> params;
    private QueryReturn returnType;
}
