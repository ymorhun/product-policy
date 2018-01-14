package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Material;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Yaroslav_Morhun
 */
public interface MaterialRepository extends CrudRepository<Material, Integer> {
}
