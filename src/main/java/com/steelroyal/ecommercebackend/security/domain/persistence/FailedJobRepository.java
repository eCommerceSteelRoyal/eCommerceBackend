package com.steelroyal.ecommercebackend.security.domain.persistence;

import com.steelroyal.ecommercebackend.security.domain.model.FailedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedJobRepository extends JpaRepository<FailedJob, Long> {
    // Aquí puedes agregar métodos para consultas personalizadas si es necesario
}