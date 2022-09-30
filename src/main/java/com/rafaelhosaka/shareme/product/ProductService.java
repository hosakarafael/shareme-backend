package com.rafaelhosaka.shareme.product;

import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.ProductNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private final FileStore fileStore;

    @Autowired
    public ProductService(ProductRepository productRepository, FileStore fileStore){
        this.productRepository = productRepository;
        this.fileStore = fileStore;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByCategory(String category) {
        Category categoryObj;
        try {
            categoryObj = Category.valueOf(category.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ArrayList<>();
        }
        return productRepository.getProductByCategory(categoryObj);
    }

    public Product createPost(Product product, MultipartFile file) {
        String fileName =  String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        product.setFileName(fileName);
        product.setDateCreated(LocalDateTime.now());
        product =  productRepository.save(product);

        try {
            fileStore.upload(
                    String.format("%s/%s", BucketName.PRODUCTS.getName(), product.getId()),
                    fileName,
                    Optional.of(fileStore.getMetadata(file)),
                    file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return product;
    }

    public byte[] downloadProductImage(String productId) throws ProductNotFoundException {
        Product product =  productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id "+productId+" not found")
        );
        return fileStore.download(
                String.format("%s/%s", BucketName.PRODUCTS.getName(), productId) ,
                product.getFileName());
    }


}
