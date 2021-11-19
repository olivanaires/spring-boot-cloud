package br.com.ota.udemy.discoveryservice.repository;

import br.com.ota.udemy.discoveryservice.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
