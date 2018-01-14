package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.PolicyExpert;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import com.morhun.product.policy.domain.composite.ProductPolicyExpertId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface PolicyExpertRepository extends CrudRepository<PolicyExpert, ProductPolicyExpertId> {
    @Query("SELECT u FROM PolicyExpert r JOIN r.id.user u WHERE r.id.productPolicy = :productPolicy")
    List<User> findExpertsByProductPolicy(@Param("productPolicy") ProductPolicy productPolicy);
}
