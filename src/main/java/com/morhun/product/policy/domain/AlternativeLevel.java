package com.morhun.product.policy.domain;

import com.morhun.product.policy.domain.composite.AlternativeLevelId;
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
public class AlternativeLevel implements Serializable {

    @EmbeddedId
    private AlternativeLevelId id;
    private int level;

    public AlternativeLevelId getId() {
        return id;
    }

    public void setId(AlternativeLevelId id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AlternativeLevel that = (AlternativeLevel) o;

        return new EqualsBuilder()
                .append(level, that.level)
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(level)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("level", level)
                .toString();
    }
}
