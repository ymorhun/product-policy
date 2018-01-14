package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.ComparisonMatrixElementId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Entity
public class ComparisonMatrixElement implements Serializable {
    @EmbeddedId
    private ComparisonMatrixElementId id;
    private double value;
    @ManyToOne(targetEntity = Alternative.class)
    @JoinColumn(name = "first_alternative_id")
    private Alternative first_alternative;
    @ManyToOne(targetEntity = Alternative.class)
    @JoinColumn(name = "second_alternative_id")
    private Alternative second_alternative;

    public ComparisonMatrixElementId getId() {
        return id;
    }

    public void setId(ComparisonMatrixElementId id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Alternative getFirst_alternative() {
        return first_alternative;
    }

    public void setFirst_alternative(Alternative first_alternative) {
        this.first_alternative = first_alternative;
    }

    public Alternative getSecond_alternative() {
        return second_alternative;
    }

    public void setSecond_alternative(Alternative second_alternative) {
        this.second_alternative = second_alternative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ComparisonMatrixElement that = (ComparisonMatrixElement) o;

        return new EqualsBuilder()
                .append(value, that.value)
                .append(id, that.id)
                .append(first_alternative, that.first_alternative)
                .append(second_alternative, that.second_alternative)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(value)
                .append(first_alternative)
                .append(second_alternative)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("value", value)
                .append("first_alternative", first_alternative)
                .append("second_alternative", second_alternative)
                .toString();
    }
}