package com.ecommerceProject.Glowi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
@TypeAlias("Category")
@Data
public class Category {
    @Id
    private String id;
    private String name;
    private String description;

}
