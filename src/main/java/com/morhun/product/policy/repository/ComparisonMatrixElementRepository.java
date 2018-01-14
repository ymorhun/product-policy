package com.morhun.product.policy.repository;

import com.morhun.product.policy.domain.ComparisonMatrix;
import com.morhun.product.policy.domain.ComparisonMatrixElement;
import com.morhun.product.policy.domain.composite.ComparisonMatrixElementId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yaroslav_Morhun
 */
@Repository
public interface ComparisonMatrixElementRepository extends CrudRepository<ComparisonMatrixElement, ComparisonMatrixElementId> {
    @Query("UPDATE ComparisonMatrixElement e SET e.value = :value WHERE e.id.comparisonMatrix = :comparisonMatrix AND e.id.row = :row AND e.id.column = :column")
    @Modifying
    void setValueOfMatrixElement(@Param("comparisonMatrix") ComparisonMatrix matrix, @Param("row") int row, @Param("column") int column, @Param("value") double value);

    @Query("SELECT m FROM ComparisonMatrixElement m WHERE m.id.comparisonMatrix = :matrix")
    List<ComparisonMatrixElement> findAllByComparisonMatrix(@Param("matrix") ComparisonMatrix comparisonMatrix);
}
