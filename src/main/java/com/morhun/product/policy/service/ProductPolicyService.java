package com.morhun.product.policy.service;

import com.morhun.product.policy.domain.PolicyExpert;
import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import com.morhun.product.policy.domain.composite.ProductPolicyExpertId;
import com.morhun.product.policy.repository.PolicyExpertRepository;
import com.morhun.product.policy.repository.ProductPolicyRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * @author Yaroslav_Morhun
 */
@Service
public class ProductPolicyService {
    @Autowired
    private ProductPolicyRepository productPolicyRepository;
    @Autowired
    private PolicyExpertRepository policyExpertRepository;

    public ProductPolicy createNewProductPolicy(ProductPolicy productPolicy) {
        return productPolicyRepository.save(productPolicy);
    }

    public void updateWithListOfExperts(ProductPolicy productPolicy, Map<User, Integer> experts) {
        for(Map.Entry<User, Integer> expertEntry : experts.entrySet()) {
            ProductPolicyExpertId id = new ProductPolicyExpertId();
            id.setProductPolicy(productPolicy);
            id.setUser(expertEntry.getKey());
            PolicyExpert policyExpert = new PolicyExpert();
            policyExpert.setId(id);
            policyExpert.setExpertPriority(expertEntry.getValue());
            policyExpertRepository.save(policyExpert);
        }
    }


    public List<ProductPolicy> getProductPoliciesInitiatedBy(User initiator) {
        return productPolicyRepository.findByInitiator(initiator);
    }

    public List<ProductPolicy> getProductPoliciesWithPersonCreatesDecision(User personCreatesDecision) {
        return productPolicyRepository.findByPersonCreatesDecision(personCreatesDecision);
    }

    public ProductPolicy getProductPolicyById(int productPolicyId) {
        return productPolicyRepository.findOne(productPolicyId);
    }

    public List<ProductPolicy> getProductPoliciesWithEmptyComparisonsForExpert(User expert) {
        return productPolicyRepository.findWithEmptyComparisons(expert);
    }

    public boolean isProductPolicyConfigured(ProductPolicy productPolicy) {
        Integer comparisonMatrixesCount = productPolicyRepository.findCountOfComparisonMatrixes(productPolicy);
        return ofNullable(comparisonMatrixesCount).orElse(0) > 1;
    }

    @Transactional
    public void updateWithDateCreation(ProductPolicy productPolicy) {
        productPolicy.setDateCreation(new Date());
        productPolicyRepository.save(productPolicy);
    }
}
