package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.Alternative;
import com.morhun.product.policy.domain.ProductPolicy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class AlternativeLevelId implements Serializable {

    @ManyToOne(targetEntity = Alternative.class)
    @JoinColumn(name = "alternative_id")
    private Alternative alternative;

    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;

    public Alternative getAlternative() {
        return alternative;
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
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

        AlternativeLevelId that = (AlternativeLevelId) o;

        return new EqualsBuilder()
                .append(alternative, that.alternative)
                .append(productPolicy, that.productPolicy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(alternative)
                .append(productPolicy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("alternative", alternative)
                .append("productPolicy", productPolicy)
                .toString();
    }
}
