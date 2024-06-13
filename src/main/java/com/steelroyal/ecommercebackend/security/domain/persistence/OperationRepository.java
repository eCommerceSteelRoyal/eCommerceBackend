package com.steelroyal.ecommercebackend.security.domain.persistence;

import com.steelroyal.ecommercebackend.security.domain.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
    Optional<Operation> findByName(String name);
}