package com.steelroyal.ecommercebackend.security.domain.persistence;

import com.steelroyal.ecommercebackend.security.resource.SecurityRule;
import com.steelroyal.ecommercebackend.security.domain.model.RoleOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleOperationRepository extends JpaRepository<RoleOperation, Integer> {
    Optional<RoleOperation> findByRoleIdAndOperationId(Integer roleId, Integer operationId);

    @Query(value = "SELECT new com.steelroyal.ecommercebackend.security.resource.SecurityRule(m, o, r) " +
            "FROM RoleOperation ro " +
            "JOIN ro.operation o " +
            "JOIN o.module m " +
            "JOIN ro.role r",
            nativeQuery = false)
    List<SecurityRule> findSecurityRules();
}
