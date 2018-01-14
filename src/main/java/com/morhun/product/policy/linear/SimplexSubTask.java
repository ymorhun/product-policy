package com.morhun.product.policy.linear;

import com.morhun.product.policy.LimitMismatchException;
import com.morhun.product.policy.domain.Product;

import java.util.Arrays;

/**
 * Created by yarki on 10.09.2017.
 */
public class SimplexSubTask {
    private SimplexTask task;
    private double[] extendedPrices;
    private int amountOfRealProducts;

    public SimplexSubTask(double[] prices, double[] holdings, double[][] expenses, Product[] products) {
        amountOfRealProducts = prices.length;
        extendedPrices = Arrays.copyOf(prices, prices.length + holdings.length);
        for(int i = prices.length; i < extendedPrices.length; i++) {
            extendedPrices[i] = -1;
        }
        double[][] matrixAExtended = new double[holdings.length][];
        int[] basicIndexes = new int[holdings.length];
        for(int row = 0; row < holdings.length; row++) {
            matrixAExtended[row] = Arrays.copyOf(expenses[row], prices.length + holdings.length);
            matrixAExtended[row][prices.length + row] = 1;
            basicIndexes[row] = prices.length + row;
        }
        task = new SimplexTask(extendedPrices, holdings, matrixAExtended, products, basicIndexes);
    }

    public double[] calculateVolumes() {
        double[] solution = task.calculateVolumesOfProducts();
        double functionValue = 0;
        for(int i = amountOfRealProducts; i < extendedPrices.length; i++) {
            functionValue += extendedPrices[i] * solution[i];
        }
        if(functionValue == 0) {
            return solution;
        } else {
            throw new LimitMismatchException();
        }
    }
}
