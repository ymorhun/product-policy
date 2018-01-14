package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Alternative;
import com.morhun.product.policy.domain.ComparisonMatrix;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface ComparisonMatrixRepository extends CrudRepository<ComparisonMatrix, Integer> {
    List<ComparisonMatrix> findAllByProductPolicyAndExpert(ProductPolicy productPolicy, User expert);

    List<ComparisonMatrix> findAllByProductPolicy(ProductPolicy productPolicy);

    ComparisonMatrix findByProductPolicyAndComparisonCriteriaAndExpert(ProductPolicy productPolicy, Alternative comparisonCriteria, User expert);
}
