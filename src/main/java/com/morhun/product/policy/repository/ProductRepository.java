package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Alternative;
import com.morhun.product.policy.domain.Criteria;
import com.morhun.product.policy.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findProductsByProductGroup(Criteria one);
}
