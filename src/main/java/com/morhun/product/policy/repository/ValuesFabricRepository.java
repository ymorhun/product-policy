package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Product;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.ValuesFabric;
import com.morhun.product.policy.domain.composite.ValuesFabricId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface ValuesFabricRepository extends CrudRepository<ValuesFabric, ValuesFabricId> {
    @Modifying
    @Query("UPDATE ValuesFabric v SET v.price = :price WHERE v.id.productPolicy = :productPolicy AND v.id.product=:product")
    void setPriceForProduct(@Param("product")Product product, @Param("productPolicy")ProductPolicy productPolicy, @Param("price") double price);

    @Query("SELECT v FROM ValuesFabric v WHERE v.id.productPolicy = :policy")
    List<ValuesFabric> findAllByProductPolicy(@Param("policy") ProductPolicy productPolicy);
}
