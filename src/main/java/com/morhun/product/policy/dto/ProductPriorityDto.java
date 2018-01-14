package com.morhun.product.policy.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by yarki on 25.06.2017.
 */
public class ProductPriorityDto {
    private int productId;
    private String name;
    private double priority;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPriorityDto that = (ProductPriorityDto) o;

        return new EqualsBuilder()
                .append(productId, that.productId)
                .append(priority, that.priority)
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(productId)
                .append(name)
                .append(priority)
                .toHashCode();
    }
}
