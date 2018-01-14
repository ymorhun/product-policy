package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Yaroslav_Morhun
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
