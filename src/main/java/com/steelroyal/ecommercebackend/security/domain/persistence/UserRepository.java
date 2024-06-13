package com.steelroyal.ecommercebackend.security.domain.persistence;

import com.steelroyal.ecommercebackend.security.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName WHERE u.email = :email")
    Integer updateUserByEmail(@Param("email") String email, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Modifying
    @Query("DELETE FROM User u WHERE u.email = :email")
    Integer deleteUserByEmail(@Param("email") String email);
}
