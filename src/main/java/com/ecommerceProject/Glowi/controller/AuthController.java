package com.ecommerceProject.Glowi.controller;

import com.ecommerceProject.Glowi.dto.AuthenticationRequest;
import com.ecommerceProject.Glowi.dto.SignupRequest;
import com.ecommerceProject.Glowi.dto.UserDto;
import com.ecommerceProject.Glowi.entity.User;
import com.ecommerceProject.Glowi.repository.UserRepository;
import com.ecommerceProject.Glowi.services.auth.AuthService;
import com.ecommerceProject.Glowi.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Incorrect username or password.\"}");
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userId", optionalUser.get().getId());
            responseData.put("userRole", optionalUser.get().getRole());
            responseData.put("jwt", jwt);

            response.setContentType("application/json");
            response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(responseData));
            response.setStatus(HttpServletResponse.SC_OK);

            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"User not found.\"}");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        //System.out.println("Email: " + signupRequest.getEmail());
        //System.out.println("Name: " + signupRequest.getName());
        //System.out.println("Password: " + signupRequest.getPassword());

        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
