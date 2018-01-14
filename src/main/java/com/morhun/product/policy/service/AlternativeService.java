package com.morhun.product.policy.service;

import com.morhun.product.policy.domain.Criteria;
import com.morhun.product.policy.domain.Product;
import com.morhun.product.policy.repository.AlternativeRepository;
import com.morhun.product.policy.repository.CriteriaRepository;
import com.morhun.product.policy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yaroslav_Morhun
 */
@Service
public class AlternativeService {
    @Autowired
    private AlternativeRepository alternativeRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addCriteria(Criteria criteria) {
        criteriaRepository.save(criteria);
    }

    public Criteria getCriteriaById(int id) {
        return criteriaRepository.findOne(id);
    }

    public void addProduct(Product product) {
        alternativeRepository.save(product);
        productRepository.save(product);
    }

    public Product getProductById(int id) {
        return productRepository.findOne(id);
    }
}
