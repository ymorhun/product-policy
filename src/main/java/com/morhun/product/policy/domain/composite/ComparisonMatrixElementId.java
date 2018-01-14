package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.ComparisonMatrix;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class ComparisonMatrixElementId implements Serializable {
    @ManyToOne(targetEntity = ComparisonMatrix.class)
    @JoinColumn(name = "comparison_matrix_id")
    private ComparisonMatrix comparisonMatrix;
    @Column(name = "\"row\"")
    private int row;
    @Column(name = "\"column\"")
    private int column;

    public ComparisonMatrix getComparisonMatrix() {
        return comparisonMatrix;
    }

    public void setComparisonMatrix(ComparisonMatrix comparisonMatrix) {
        this.comparisonMatrix = comparisonMatrix;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ComparisonMatrixElementId that = (ComparisonMatrixElementId) o;

        return new EqualsBuilder()
                .append(row, that.row)
                .append(column, that.column)
                .append(comparisonMatrix, that.comparisonMatrix)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(comparisonMatrix)
                .append(row)
                .append(column)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("comparisonMatrix", comparisonMatrix)
                .append("row", row)
                .append("column", column)
                .toString();
    }
}
