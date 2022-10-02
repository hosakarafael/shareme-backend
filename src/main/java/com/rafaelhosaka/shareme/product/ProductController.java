package com.rafaelhosaka.shareme.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.ProductNotFoundException;
import com.rafaelhosaka.shareme.post.Post;
import com.rafaelhosaka.shareme.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok().body(productService.getAll());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("category") String category){
        return ResponseEntity.ok().body(productService.getByCategory(category));
    }

    @PostMapping(path = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Product> createProduct(@RequestPart("product") String json, @RequestPart(value = "file", required = false) MultipartFile file){
        Product product = null;
        try {
            product = (Product) JsonConverter.convertJsonToObject(json, Product.class);
            return ResponseEntity.ok().body(productService.createPost(product, file));
        } catch (JsonProcessingException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<List<String>> downloadPostImage(@PathVariable("id") String id)  {
        try {
            return ResponseEntity.ok().body(productService.downloadProductImage(id));
        }catch (ProductNotFoundException | IllegalStateException e) {
            return null;
        }
    }

    @GetMapping("/category")
    public ResponseEntity<Category[]> getCategories(){
        return ResponseEntity.ok().body(Category.values());
    }

    @GetMapping("/currency")
    public ResponseEntity<Currency[]> getCurrencies(){
        return ResponseEntity.ok().body(Currency.values());
    }
}
