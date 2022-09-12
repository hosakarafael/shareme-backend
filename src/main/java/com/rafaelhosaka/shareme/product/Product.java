package com.rafaelhosaka.shareme.product;

import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Product {
    @Id
    private String id;

    private String title;

    private String description;

    private Category category;

    private Currency currency;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    private LocalDateTime dateCreated;

    private String fileName;

    @DBRef
    private UserProfile user;
}
