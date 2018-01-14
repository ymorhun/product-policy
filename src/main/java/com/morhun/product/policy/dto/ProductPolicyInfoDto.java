package com.morhun.product.policy.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * @author Yaroslav_Morhun
 */
public class ProductPolicyInfoDto {
    private int productPolicyId;
    private String name;
    private String description;
    private Date initiated;
    private Date finished;
    private int duration;

    public int getProductPolicyId() {
        return productPolicyId;
    }

    public void setProductPolicyId(int productPolicyId) {
        this.productPolicyId = productPolicyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInitiated() {
        return initiated;
    }

    public void setInitiated(Date initiated) {
        this.initiated = initiated;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPolicyInfoDto that = (ProductPolicyInfoDto) o;

        return new EqualsBuilder()
                .append(productPolicyId, that.productPolicyId)
                .append(duration, that.duration)
                .append(name, that.name)
                .append(description, that.description)
                .append(initiated, that.initiated)
                .append(finished, that.finished)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(productPolicyId)
                .append(name)
                .append(description)
                .append(initiated)
                .append(finished)
                .append(duration)
                .toHashCode();
    }
}