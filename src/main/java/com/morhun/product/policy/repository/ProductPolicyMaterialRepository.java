package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Material;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.ProductPolicyMaterial;
import com.morhun.product.policy.domain.composite.ProductPolicyMaterialId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface ProductPolicyMaterialRepository extends CrudRepository<ProductPolicyMaterial, ProductPolicyMaterialId> {
    @Query("SELECT m FROM ProductPolicyMaterial m WHERE m.id.productPolicy = :policy")
    List<ProductPolicyMaterial> findAllByProductPolicy(@Param("policy") ProductPolicy productPolicy);
}
