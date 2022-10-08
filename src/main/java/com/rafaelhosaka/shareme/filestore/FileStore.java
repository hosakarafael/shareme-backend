package com.rafaelhosaka.shareme.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@PropertySource("classpath:application.properties")
public class FileStore {
    private final AmazonS3 s3;
    private Environment environment;

    @Autowired
    public FileStore(AmazonS3 s3, Environment environment) {
        this.environment = environment;
        this.s3 = s3;
    }

    public void upload(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetadata,
                     InputStream inputStream){
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try{
            s3.putObject(environment.getProperty("aws.bucket.name") + path,  fileName, inputStream, metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3",e);
        }
    }

    public S3Object download(String path, String key){
        try{
            if(key == null || key.isEmpty() ) {
                return null;
            }else{
                return s3.getObject(environment.getProperty("aws.bucket.name")  + path, key);
            }
        }catch (Exception e){
            throw  new IllegalStateException(e);
        }
    }

    public void delete(String path, String key){
        s3.deleteObject(path, key);
    }

    public Map<String, String> getMetadata(MultipartFile file){
        Map<String, String> metadata =  new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return  metadata;
    }

}
