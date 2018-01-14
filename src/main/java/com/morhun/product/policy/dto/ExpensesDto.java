package com.morhun.product.policy.dto;

import java.util.Map;

/**
 * Created by yarki on 28.06.2017.
 */
public class ExpensesDto {
    private double reserve;
    private Map<Integer, Double> productIdToExpenses;

    public double getReserve() {
        return reserve;
    }

    public void setReserve(double reserve) {
        this.reserve = reserve;
    }

    public Map<Integer, Double> getProductIdToExpenses() {
        return productIdToExpenses;
    }

    public void setProductIdToExpenses(Map<Integer, Double> productIdToExpenses) {
        this.productIdToExpenses = productIdToExpenses;
    }
}
