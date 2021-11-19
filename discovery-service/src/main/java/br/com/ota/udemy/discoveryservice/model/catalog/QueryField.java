package br.com.ota.udemy.discoveryservice.model.catalog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryField {
    private String type;
    private String name;
    private String description;
}
