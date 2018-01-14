package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.Role;
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
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role")Role role);
}
