package com.rafaelhosaka.shareme.product;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.rafaelhosaka.shareme.bucket.BucketName;
import com.rafaelhosaka.shareme.exception.PostNotFoundException;
import com.rafaelhosaka.shareme.exception.ProductNotFoundException;
import com.rafaelhosaka.shareme.filestore.FileStore;
import com.rafaelhosaka.shareme.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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

    public List<String> downloadProductImage(String productId) throws ProductNotFoundException {
        Product product =  productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id "+productId+" not found")
        );
        List returnData = new ArrayList();
        try {
            S3Object object = fileStore.download(
                    String.format("%s/%s", BucketName.PRODUCTS.getName(), productId),
                    product.getFileName());
            byte[] encoded = object == null ? new byte[0] : Base64.getEncoder().encode(IOUtils.toByteArray(object.getObjectContent()));
            returnData.add(new String(encoded, StandardCharsets.US_ASCII));
            returnData.add(object == null ? "" : object.getObjectMetadata().getUserMetadata().get("content-type"));
            return returnData;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }


}
