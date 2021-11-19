package br.com.ota.udemy.discoveryservice.model.catalog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Product {
    private String name;
    private List<Query> queries;
}
