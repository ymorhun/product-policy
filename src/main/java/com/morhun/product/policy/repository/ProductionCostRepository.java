package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Material;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.ProductionCost;
import com.morhun.product.policy.domain.composite.ProductionCostId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface ProductionCostRepository extends CrudRepository<ProductionCost, ProductionCostId> {
    @Query("SELECT c FROM ProductionCost c WHERE c.id.productPolicy = :policy")
    List<ProductionCost> findAllByProductPolicy(@Param("policy") ProductPolicy productPolicy);

    @Query("SELECT c FROM ProductionCost c WHERE c.id.productPolicy = :policy AND c.id.material = :material")
    List<ProductionCost> findAllByProductPolicyAndMaterial(@Param("policy") ProductPolicy productPolicy, @Param("material") Material material);
}
