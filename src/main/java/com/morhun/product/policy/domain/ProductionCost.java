package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.ProductionCostId;
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
public class ProductionCost implements Serializable {
    @EmbeddedId
    private ProductionCostId id;
    private double volumePerPiece;

    public ProductionCostId getId() {
        return id;
    }

    public void setId(ProductionCostId id) {
        this.id = id;
    }

    public double getVolumePerPiece() {
        return volumePerPiece;
    }

    public void setVolumePerPiece(double volumePerPiece) {
        this.volumePerPiece = volumePerPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductionCost that = (ProductionCost) o;

        return new EqualsBuilder()
                .append(volumePerPiece, that.volumePerPiece)
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(volumePerPiece)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("volumePerPiece", volumePerPiece)
                .toString();
    }
}
