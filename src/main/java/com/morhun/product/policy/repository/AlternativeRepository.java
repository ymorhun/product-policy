package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Alternative;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface AlternativeRepository extends CrudRepository<Alternative, Integer> {
}
