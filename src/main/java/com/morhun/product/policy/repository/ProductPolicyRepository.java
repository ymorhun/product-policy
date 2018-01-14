package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
public interface ProductPolicyRepository extends CrudRepository<ProductPolicy, Integer> {
    List<ProductPolicy> findByInitiator(User initiator);

    List<ProductPolicy> findByPersonCreatesDecision(User personCreatesDecision);

    @Query("SELECT DISTINCT (m.productPolicy) FROM ComparisonMatrix m, PolicyExpert pe  WHERE m.productPolicy = pe.id.productPolicy AND m.dateCreation IS NULL AND pe.id.user = :expert")
    List<ProductPolicy> findWithEmptyComparisons(@Param("expert") User user);

    @Query("SELECT COUNT (m) FROM ComparisonMatrix m WHERE m.productPolicy = :productPolicy")
    Integer findCountOfComparisonMatrixes(@Param("productPolicy") ProductPolicy productPolicy);
}
