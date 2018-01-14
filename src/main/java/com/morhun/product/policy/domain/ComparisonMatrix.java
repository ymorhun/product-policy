package com.morhun.product.policy.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Yaroslav_Morhun
 */
@Entity
public class ComparisonMatrix implements Serializable {
    @Id
    @Column(name = "comparison_matrix_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateInitiation;
    private Date dateCreation;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User expert;
    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;
    @ManyToOne(targetEntity = Alternative.class)
    @JoinColumn(name = "alternative_id")
    private Alternative comparisonCriteria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateInitiation() {
        return dateInitiation;
    }

    public void setDateInitiation(Date dateInitiation) {
        this.dateInitiation = dateInitiation;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getExpert() {
        return expert;
    }

    public void setExpert(User expert) {
        this.expert = expert;
    }

    public ProductPolicy getProductPolicy() {
        return productPolicy;
    }

    public void setProductPolicy(ProductPolicy productPolicy) {
        this.productPolicy = productPolicy;
    }

    public Alternative getComparisonCriteria() {
        return comparisonCriteria;
    }

    public void setComparisonCriteria(Alternative comparisonCriteria) {
        this.comparisonCriteria = comparisonCriteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ComparisonMatrix that = (ComparisonMatrix) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(dateInitiation, that.dateInitiation)
                .append(dateCreation, that.dateCreation)
                .append(expert, that.expert)
                .append(productPolicy, that.productPolicy)
                .append(comparisonCriteria, that.comparisonCriteria)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(dateInitiation)
                .append(dateCreation)
                .append(expert)
                .append(productPolicy)
                .append(comparisonCriteria)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("dateInitiation", dateInitiation)
                .append("dateCreation", dateCreation)
                .append("expert", expert)
                .append("productPolicy", productPolicy)
                .append("comparisonCriteria", comparisonCriteria)
                .toString();
    }
}