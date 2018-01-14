package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.Product;
import com.morhun.product.policy.domain.ProductPolicy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class ValuesFabricId implements Serializable {
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "alternative_id")
    private Product product;
    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductPolicy getProductPolicy() {
        return productPolicy;
    }

    public void setProductPolicy(ProductPolicy productPolicy) {
        this.productPolicy = productPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ValuesFabricId that = (ValuesFabricId) o;

        return new EqualsBuilder()
                .append(product, that.product)
                .append(productPolicy, that.productPolicy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(product)
                .append(productPolicy)
                .toHashCode();
    }
}
