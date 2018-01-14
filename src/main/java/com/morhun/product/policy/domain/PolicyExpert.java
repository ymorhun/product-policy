package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.ProductPolicyExpertId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Entity
public class PolicyExpert implements Serializable {
    @EmbeddedId
    private ProductPolicyExpertId id;
    private int expertPriority;

    public ProductPolicyExpertId getId() {
        return id;
    }

    public void setId(ProductPolicyExpertId id) {
        this.id = id;
    }

    public int getExpertPriority() {
        return expertPriority;
    }

    public void setExpertPriority(int expertPriority) {
        this.expertPriority = expertPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PolicyExpert that = (PolicyExpert) o;

        return new EqualsBuilder()
                .append(expertPriority, that.expertPriority)
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(expertPriority)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("expertPriority", expertPriority)
                .toString();
    }
}