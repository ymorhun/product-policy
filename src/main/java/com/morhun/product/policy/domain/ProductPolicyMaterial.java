package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.ProductPolicyMaterialId;
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
public class ProductPolicyMaterial implements Serializable {

    @EmbeddedId
    private ProductPolicyMaterialId id;
    private double volume;
    private double maxIncrease;
    private double maxDecrease;

    public ProductPolicyMaterialId getId() {
        return id;
    }

    public void setId(ProductPolicyMaterialId id) {
        this.id = id;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getMaxIncrease() {
        return maxIncrease;
    }

    public void setMaxIncrease(double maxIncrease) {
        this.maxIncrease = maxIncrease;
    }

    public double getMaxDecrease() {
        return maxDecrease;
    }

    public void setMaxDecrease(double maxDecrease) {
        this.maxDecrease = maxDecrease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPolicyMaterial that = (ProductPolicyMaterial) o;

        return new EqualsBuilder()
                .append(volume, that.volume)
                .append(maxIncrease, that.maxIncrease)
                .append(maxDecrease, that.maxDecrease)
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(volume)
                .append(maxIncrease)
                .append(maxDecrease)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("volume", volume)
                .append("maxIncrease", maxIncrease)
                .append("maxDecrease", maxDecrease)
                .toString();
    }
}
