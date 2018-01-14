package com.morhun.product.policy.service;

import com.morhun.product.policy.domain.Role;
import com.morhun.product.policy.domain.User;
import com.morhun.product.policy.repository.RoleRepository;
import com.morhun.product.policy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Service
public class UserService {
    private static final int PERSON_CREATES_DECISION_ROLE_ID = 1;
    private static final int EXPERT_ROLE_ID = 3;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public User getUserByCredentials(String login, String password) {
        User user = userRepository.findByEmailAndPassword(login, password);
        if(user == null) {
            throw new IllegalArgumentException("No users with such credentials");
        }
        return user;
    }

    public User getUserById(int userId) {
        return userRepository.findOne(userId);
    }

    public List<User> findPersonsCreateDecisions() {
        Role personCreatesDecisionRole = roleRepository.findOne(PERSON_CREATES_DECISION_ROLE_ID);
        return userRepository.findByRole(personCreatesDecisionRole);
    }

    public List<User> findAvailableExperts() {
        Role expert = roleRepository.findOne(EXPERT_ROLE_ID);
        return userRepository.findByRole(expert);
    }
}
