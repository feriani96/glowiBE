package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.enums.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

@Document(collection = "user")
@TypeAlias("User")
@Data
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private String name;

    private UserRole role;

    private byte[] img;
}
