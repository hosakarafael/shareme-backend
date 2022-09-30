package com.rafaelhosaka.shareme.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ 'category' : ?0 }")
    List<Product> getProductByCategory(Category category);
}
