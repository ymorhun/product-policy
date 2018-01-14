package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class ProductPolicyExpertId implements Serializable {
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

        ProductPolicyExpertId that = (ProductPolicyExpertId) o;

        return new EqualsBuilder()
                .append(user, that.user)
                .append(productPolicy, that.productPolicy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(user)
                .append(productPolicy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("productPolicy", productPolicy)
                .toString();
    }
}