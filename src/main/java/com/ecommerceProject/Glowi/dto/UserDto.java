package com.ecommerceProject.Glowi.dto;

import com.ecommerceProject.Glowi.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String email;
    private String name;
    private UserRole userRole;
}
