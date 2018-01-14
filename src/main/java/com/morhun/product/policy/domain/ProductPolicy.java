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
public class ProductPolicy implements Serializable {
    @Id
    @Column(name = "product_policy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateInitiation;
    private Date dateCreation;
    private String name;
    private String description;
    private int duration;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "initiator")
    private User initiator;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "person_creates_decision")
    private User personCreatesDecision;

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getPersonCreatesDecision() {
        return personCreatesDecision;
    }

    public void setPersonCreatesDecision(User personCreatesDecision) {
        this.personCreatesDecision = personCreatesDecision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPolicy that = (ProductPolicy) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(duration, that.duration)
                .append(dateInitiation, that.dateInitiation)
                .append(dateCreation, that.dateCreation)
                .append(name, that.name)
                .append(description, that.description)
                .append(initiator, that.initiator)
                .append(personCreatesDecision, that.personCreatesDecision)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(dateInitiation)
                .append(dateCreation)
                .append(name)
                .append(description)
                .append(duration)
                .append(initiator)
                .append(personCreatesDecision)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("dateInitiation", dateInitiation)
                .append("dateCreation", dateCreation)
                .append("name", name)
                .append("description", description)
                .append("duration", duration)
                .append("initiator", initiator)
                .append("person who creates decision", personCreatesDecision)
                .toString();
    }
}
