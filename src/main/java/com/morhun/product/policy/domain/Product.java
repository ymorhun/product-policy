package com.morhun.product.policy.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Entity
public class Product extends Alternative implements Serializable {
    @ManyToOne(targetEntity = Criteria.class)
    @JoinColumn(name = "product_group_id")
    private Criteria productGroup;

    public Criteria getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(Criteria productGroup) {
        this.productGroup = productGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return new EqualsBuilder()
                .append(getId(), product.getId())
                .append(getTitle(), product.getTitle())
                .append(productGroup, product.productGroup)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getTitle())
                .append(productGroup)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("title", getTitle())
                .append("productGroup", productGroup)
                .toString();
    }
}