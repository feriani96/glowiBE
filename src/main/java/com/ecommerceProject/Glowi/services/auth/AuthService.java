package com.ecommerceProject.Glowi.services.auth;

import com.ecommerceProject.Glowi.dto.SignupRequest;
import com.ecommerceProject.Glowi.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
}
