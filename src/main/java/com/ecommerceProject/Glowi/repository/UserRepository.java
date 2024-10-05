package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.User;
import com.ecommerceProject.Glowi.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findFirstByEmail(String email);

    User findByRole(UserRole userRole);
}
