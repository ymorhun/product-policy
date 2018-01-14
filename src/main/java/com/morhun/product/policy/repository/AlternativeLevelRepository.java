package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Alternative;
import com.morhun.product.policy.domain.AlternativeLevel;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.composite.AlternativeLevelId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface AlternativeLevelRepository extends CrudRepository<AlternativeLevel, AlternativeLevelId> {
    @Query("SELECT a FROM AlternativeLevel a WHERE a.id.productPolicy = :policy")
    List<AlternativeLevel> findByProductPolicy(@Param("policy") ProductPolicy productPolicy);

    @Query("SELECT a FROM AlternativeLevel a WHERE a.id.productPolicy = :policy AND a.id.alternative = :alternative")
    AlternativeLevel findByProductPolicyAndAlternative(@Param("policy") ProductPolicy productPolicy, @Param("alternative") Alternative alternative);

    @Query("SELECT a FROM AlternativeLevel a WHERE a.id.productPolicy = :policy AND a.level = :level")
    List<AlternativeLevel> findByProductPolicyAndLevel(@Param("policy") ProductPolicy productPolicy, @Param("level") int level);
}
