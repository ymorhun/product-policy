package com.morhun.product.policy.linear;

import com.morhun.product.policy.domain.Product;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

/**
 * Created by yarki on 10.09.2017.
 */
public class SimplexTask {
    private final int[] basicIndexes;
    private Product[] products;
    private int valuableVariablesAmount;
    private int amountOfProducts;

    private double[][] mainTable;
    private double[][] secondaryTable;

    public SimplexTask(double[] prices, double[] holdings, double[][] expenses, Product[] products, int[] basicIndexes) {
        this.products = products;
        this.valuableVariablesAmount = holdings.length;
        secondaryTable = new double[holdings.length + 2][1 + prices.length];
        mainTable = new double[holdings.length + 1][5 + holdings.length];
        this.basicIndexes = basicIndexes;
        amountOfProducts = prices.length;
        fillSecondaryTable(prices, holdings, expenses);
        fillMainTable(holdings, expenses, prices);
    }

    private void fillSecondaryTable(double[] prices, double[] holdings, double[][] expenses) {
        for(int row = 0; row < holdings.length; row++) {
            secondaryTable[row][0] = holdings[row];
            for (int productIndex = 0; productIndex < prices.length; productIndex++) {
                secondaryTable[row][1 + productIndex] = expenses[row][productIndex];
            }
        }
        int pricesRow = holdings.length;
        System.arraycopy(prices, 0, secondaryTable[pricesRow], 1, prices.length);
    }

    private void fillMainTable(double[] holdings, double[][] expenses, double[]prices) {
        double[][] Afx = new double[holdings.length][holdings.length];
        for(int column = 0; column < holdings.length; column++) {
            for(int row = 0; row < holdings.length; row++) {
                Afx[row][column] = expenses[row][basicIndexes[column]];
            }
        }
        SimpleMatrix matrixAfx = new SimpleMatrix(Afx);
        matrixAfx = matrixAfx.invert();
        SimpleMatrix e0 = matrixAfx.mult(SimpleMatrix.wrap(DenseMatrix64F.wrap(holdings.length, 1, holdings)));
        for(int i = 0; i < holdings.length; i++) {
            mainTable[i][0] = prices[basicIndexes[i]];
            mainTable[i][1] = basicIndexes[i];
            mainTable[i][2] = e0.get(i);
            for(int j = 0; j < holdings.length; j++) {
                mainTable[i][3 + j] = matrixAfx.get(i, j);
            }
        }
    }

    public double[] calculateVolumesOfProducts() {
        int lastRowInMainTable = getIndexOfLastRow(mainTable);
        if(lastRowInMainTable == mainTable.length) {
            mainTable = Arrays.copyOf(mainTable, mainTable.length + 1);
            mainTable[lastRowInMainTable] = new double[valuableVariablesAmount + 5];
        }
        fillLambdas(lastRowInMainTable);
        do {
            int lastRowInSecondaryTable = getIndexOfLastRow(secondaryTable);
            if (lastRowInSecondaryTable == secondaryTable.length) {
                secondaryTable = Arrays.copyOf(secondaryTable, secondaryTable.length + 1);
                secondaryTable[lastRowInSecondaryTable] = new double[secondaryTable[0].length];
            }
            double[] deltas = findDeltas(lastRowInMainTable);
            secondaryTable[lastRowInSecondaryTable] = new double[1 + amountOfProducts];
            System.arraycopy(deltas, 0, secondaryTable[lastRowInSecondaryTable], 1, deltas.length);
            if (Arrays.stream(deltas).allMatch(value -> value >= 0)) {
                return getSolution();
            }
            double minDelta = Arrays.stream(deltas).min().orElseThrow(RuntimeException::new);
            int indexOfMinDelta = ArrayUtils.indexOf(deltas, minDelta);
            double minTetha = Double.MAX_VALUE;
            int indexOfVectorRemoveFromBase = -1;
            for (int i = 0; i < valuableVariablesAmount; i++) {
                double x_i_k = 0;
                for (int j = 0; j < valuableVariablesAmount; j++) {
                    x_i_k += mainTable[i][3 + j] * secondaryTable[j][1 + indexOfMinDelta];
                }
                mainTable[i][3 + valuableVariablesAmount] = x_i_k;
                if (x_i_k > 0) {
                    double teta = mainTable[i][2] / x_i_k;
                    if (teta < minTetha) {
                        minTetha = teta;
                        indexOfVectorRemoveFromBase = i;
                    }
                }
            }
            mainTable[lastRowInMainTable][3 + valuableVariablesAmount] = secondaryTable[lastRowInSecondaryTable][1 + indexOfMinDelta];
            recalculateMainTable(indexOfMinDelta, indexOfVectorRemoveFromBase);
        }while (true);
    }

    private void recalculateMainTable(int indexNewVector, int indexRemovedVector) {
        mainTable[indexRemovedVector][0] = secondaryTable[valuableVariablesAmount][1 + indexNewVector];
        mainTable[indexRemovedVector][1] = indexNewVector + 1;
        basicIndexes[indexRemovedVector] = indexNewVector;
        double x_r_k = mainTable[indexRemovedVector][3 + valuableVariablesAmount];
        for(int i = 0; i <= valuableVariablesAmount; i++) {
            if (i != indexRemovedVector) {
                for (int j = 0; j <= valuableVariablesAmount; j++) {
                    mainTable[i][2 + j] -= mainTable[indexRemovedVector][2 + j] * mainTable[i][3 + valuableVariablesAmount] / x_r_k;
                }
            }
        }
        for (int j = 0; j <= valuableVariablesAmount; j++) {
            mainTable[indexRemovedVector][2 + j] /= x_r_k;
        }
    }

    private double[] getSolution() {
        double[] solution = new double[amountOfProducts];
        for(int i = 0; i < valuableVariablesAmount; i++) {
            solution[basicIndexes[i]] = mainTable[i][2];
        }
        return solution;
    }

    private double[] findDeltas(int lastRowInMainTable) {
        double[] deltas = new double[amountOfProducts];
        double[] lambdas = new double[valuableVariablesAmount];
        System.arraycopy(mainTable[lastRowInMainTable], 3, lambdas, 0, valuableVariablesAmount);
        DenseMatrix64F lambdasMatrix = DenseMatrix64F.wrap(1, lambdas.length, lambdas);
        for(int i = 0; i < amountOfProducts; i++) {
            double[] aColumn = new double[valuableVariablesAmount];
            for(int j = 0; j < valuableVariablesAmount; j++) {
                aColumn[j] = secondaryTable[j][i + 1];
            }
            DenseMatrix64F result = new DenseMatrix64F(lambdasMatrix.getNumRows(), 1);
            CommonOps.mult(lambdasMatrix, DenseMatrix64F.wrap(aColumn.length, 1, aColumn), result);
            deltas[i] = result.get(0) - secondaryTable[valuableVariablesAmount][1 + i];
        }
        return deltas;
    }

    private int getIndexOfLastRow(double[][] matrix) {
        for(int row = 0; row < matrix.length; row++) {
            if(Arrays.stream(matrix[row]).allMatch(value -> value == 0)) {
                return row;
            }
        }
        return matrix.length;
    }

    private void fillLambdas(int row) {
        for(int i = 0; i < valuableVariablesAmount + 1; i++) {
            double lambda = 0;
            for(int j = 0; j < valuableVariablesAmount; j++) {
                double Cs = mainTable[j][0];
                double e = mainTable[j][2 + i];
                lambda += Cs * e;
            }
            mainTable[row][2 + i] = lambda;
        }
    }
}
