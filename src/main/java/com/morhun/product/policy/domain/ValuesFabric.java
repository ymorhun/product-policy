package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.ValuesFabricId;
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
public class ValuesFabric implements Serializable {
    @EmbeddedId
    private ValuesFabricId id;
    private double volume;
    private double price;

    public ValuesFabricId getId() {
        return id;
    }

    public void setId(ValuesFabricId id) {
        this.id = id;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ValuesFabric that = (ValuesFabric) o;

        return new EqualsBuilder()
                .append(volume, that.volume)
                .append(price, that.price)
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(volume)
                .append(price)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("volume", volume)
                .append("price", price)
                .toString();
    }
}