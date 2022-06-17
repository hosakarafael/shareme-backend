package com.rafaelhosaka.shareme.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.rafaelhosaka.shareme.bucket.BucketName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
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
            s3.putObject(path, fileName, inputStream, metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3",e);
        }
    }

    public byte[] download(String path, String key){
        try{
            if(key == null || key.isEmpty() ) {
                return new byte[0];
            }else{
                S3Object object = s3.getObject(path, key);
                return IOUtils.toByteArray(object.getObjectContent());
            }
        }catch (Exception e){
            throw  new IllegalStateException(e);
        }
    }

    public Map<String, String> getMetadata(MultipartFile file){
        Map<String, String> metadata =  new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return  metadata;
    }
}
