package br.com.ota.udemy.discoveryservice.model.catalog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QueryReturn {
    private String type;
    private String name;
    private List<QueryField> fields;
}
