package com.morhun.product.policy.service;

import com.google.common.collect.Lists;
import com.morhun.product.policy.domain.*;
import com.morhun.product.policy.domain.composite.AlternativeLevelId;
import com.morhun.product.policy.domain.composite.ComparisonMatrixElementId;
import com.morhun.product.policy.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * @author Yaroslav_Morhun
 */
@Service
public class HierarchyService {
    private static final double[] AVERAGE_LEVEL_OF_MATRIX_CONCERTNESS = {0d,0d, 0.58d, 0.9d, 1.12d, 1.24d, 1.32d, 1.41d, 1.45d, 1.49d, 1.51d, 1.48d, 1.56d};
    @Autowired
    private AlternativeLevelRepository alternativeLevelRepository;

    @Autowired
    private PolicyExpertRepository policyExpertRepository;

    @Autowired
    private ComparisonMatrixRepository comparisonMatrixRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ComparisonMatrixElementRepository comparisonMatrixElementRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    public void createNewHierarchyForProductPolicy(ProductPolicy productPolicy, Map<Criteria, Integer> alternativeLevels) {
        for (Map.Entry<Criteria, Integer> alternative : alternativeLevels.entrySet()) {
            AlternativeLevelId id = new AlternativeLevelId();
            id.setAlternative(alternative.getKey());
            id.setProductPolicy(productPolicy);
            AlternativeLevel alternativeLevel = new AlternativeLevel();
            alternativeLevel.setId(id);
            alternativeLevel.setLevel(alternative.getValue());
            alternativeLevelRepository.save(alternativeLevel);
        }
    }

    public void updateHierarchyWithProducts(ProductPolicy productPolicy, List<Product> products) {
        int maxLevel = alternativeLevelRepository.findByProductPolicy(productPolicy).stream().mapToInt(AlternativeLevel::getLevel).max().orElse(-1);
        if(maxLevel > 0) {
            int productLevel = maxLevel + 1;
            for(Product product : products) {
                AlternativeLevelId id = new AlternativeLevelId();
                id.setProductPolicy(productPolicy);
                id.setAlternative(product);
                AlternativeLevel alternativeLevel = new AlternativeLevel();
                alternativeLevel.setId(id);
                alternativeLevel.setLevel(productLevel);
                alternativeLevelRepository.save(alternativeLevel);
            }
        }
    }

    public void createComparisonMatrixForProductPolicy(ProductPolicy productPolicy) {
        List<User> experts = policyExpertRepository.findExpertsByProductPolicy(productPolicy);
        List<AlternativeLevel> hierarchyElements = alternativeLevelRepository
                .findByProductPolicy(productPolicy).stream()
                .filter(alternativeLevel -> !productRepository.exists(alternativeLevel.getId().getAlternative().getId()))
                .collect(toList());
        for (User expert : experts) {
            for (AlternativeLevel alternativeLevel : hierarchyElements) {
                ComparisonMatrix comparisonMatrixForAlternative = createComparisonMatrixForAlternative(productPolicy, expert, alternativeLevel.getId().getAlternative());
                createMatrixElements(comparisonMatrixForAlternative, alternativeLevel.getId().getAlternative());
            }
        }
        for (Alternative alternative : hierarchyElements.stream().map(alternativeLevel -> alternativeLevel.getId().getAlternative()).collect(toList())) {
            ComparisonMatrix comparisonMatrixForAlternative = createComparisonMatrixForAlternative(productPolicy, null, alternative);
            createMatrixElements(comparisonMatrixForAlternative, alternative);
        }
    }

    private ComparisonMatrix createComparisonMatrixForAlternative(ProductPolicy productPolicy, User expert, Alternative alternative) {
        ComparisonMatrix comparisonMatrix = new ComparisonMatrix();
        comparisonMatrix.setExpert(expert);
        comparisonMatrix.setComparisonCriteria(alternative);
        comparisonMatrix.setProductPolicy(productPolicy);
        comparisonMatrix.setDateInitiation(new Date());
        return comparisonMatrixRepository.save(comparisonMatrix);
    }

    private void createMatrixElements(ComparisonMatrix matrix, Alternative alternative) {
        AlternativeLevel alternativeLevel = alternativeLevelRepository.findByProductPolicyAndAlternative(matrix.getProductPolicy(), alternative);
        List<Alternative> alternativesToCompare = alternativeLevelRepository
                .findByProductPolicyAndLevel(matrix.getProductPolicy(), alternativeLevel.getLevel() + 1)
                .stream().sorted(comparingInt(o -> o.getId().getAlternative().getId()))
                .map(al -> al.getId().getAlternative())
                .collect(toList());
        boolean isProducts = alternativesToCompare.stream().allMatch(al -> productRepository.exists(al.getId()));
        if(isProducts) {
            Iterable<Product> products = productRepository
                    .findAll(alternativesToCompare.stream().map(Alternative::getId).collect(toList()));
            alternativesToCompare = Lists.newArrayList(products).stream()
                    .filter(product -> product.getProductGroup().getId() == alternative.getId())
                    .sorted(comparingInt(Alternative::getId))
                    .collect(toList());
        }
        for(int i = 0; i < alternativesToCompare.size(); i++) {
            for(int j = i; j < alternativesToCompare.size(); j++) {
                ComparisonMatrixElementId id = new ComparisonMatrixElementId();
                id.setComparisonMatrix(matrix);
                id.setRow(i);
                id.setColumn(j);
                ComparisonMatrixElement element = new ComparisonMatrixElement();
                element.setId(id);
                element.setFirst_alternative(alternativesToCompare.get(i));
                element.setSecond_alternative(alternativesToCompare.get(j));
                comparisonMatrixElementRepository.save(element);
                if (j == i) {
                    continue;
                }
                ComparisonMatrixElementId reverseId = new ComparisonMatrixElementId();
                reverseId.setComparisonMatrix(matrix);
                reverseId.setRow(j);
                reverseId.setColumn(i);
                ComparisonMatrixElement reverseElement = new ComparisonMatrixElement();
                reverseElement.setId(reverseId);
                reverseElement.setFirst_alternative(alternativesToCompare.get(j));
                reverseElement.setSecond_alternative(alternativesToCompare.get(i));
                comparisonMatrixElementRepository.save(reverseElement);
            }
        }
    }

    public void updateComparisonMatrixWithDateCreation(ComparisonMatrix comparisonMatrix, Date date) {
        comparisonMatrix.setDateCreation(date);
        comparisonMatrixRepository.save(comparisonMatrix);
    }

    public boolean areAllMatricesFilled(ProductPolicy productPolicy) {
        List<User> experts = policyExpertRepository.findExpertsByProductPolicy(productPolicy);
        for(User user : experts) {
            if(!areAllMatricesForUserFilled(productPolicy, user)) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllMatricesForUserFilled(ProductPolicy productPolicy, User expert) {
        List<ComparisonMatrix> matricesForUserAndProductPolicy = comparisonMatrixRepository.findAllByProductPolicyAndExpert(productPolicy, expert);
        return matricesForUserAndProductPolicy.stream()
                .allMatch(comparisonMatrix -> nonNull(comparisonMatrix.getDateCreation()) && comparisonMatrix.getDateCreation().after(comparisonMatrix.getDateInitiation()));
    }

    public double[][] getCommonMatrixForAlternative(ProductPolicy productPolicy, Alternative alternative) {
        ComparisonMatrix comparisonMatrix = comparisonMatrixRepository.findByProductPolicyAndComparisonCriteriaAndExpert(productPolicy, alternative, null);
        return getArrayOfMarks(comparisonMatrix);
    }

    public double[] getLocalPriorities(double[][] matrix) {
        double[][] localPrioritiesForDifferentMethods = new double[][] {getPrioritiesBySumInRow(matrix),
                getPrioritiesByInvertedSumInRow(matrix),
                getPrioritiesByColumnNormalization(matrix),
                getPrioritiesByMultiplyingElements(matrix)};
        double[] localPriories = new double[matrix.length];
        for (int i = 0; i < localPriories.length; i++) {
            int j = 0;
            double resultPriority = 1;
            while(j < localPrioritiesForDifferentMethods.length) {
                resultPriority *= localPrioritiesForDifferentMethods[j][i];
                j++;
            }
            localPriories[i] = Math.pow(resultPriority, 1.0/j);
        }
        return localPriories;
    }

    private double[] getPrioritiesBySumInRow(double[][] matrix) {
        int prioritiesAmount = matrix.length;
        double[] localPriorities = new double[prioritiesAmount];
        SimpleMatrix simpleMatrix = new SimpleMatrix(matrix);
        SimpleMatrix sumsOfRows = new SimpleMatrix(CommonOps.sumRows(simpleMatrix.getMatrix(), null));
        double sumOfElements = CommonOps.elementSum(simpleMatrix.getMatrix());
        for(int i = 0; i < prioritiesAmount; i++) {
            localPriorities[i] = sumsOfRows.get(i)/sumOfElements;
        }
        return localPriorities;
    }

    private double[] getPrioritiesByInvertedSumInRow(double[][] matrix) {
        int prioritiesAmount = matrix.length;
        double[] localPriorities = new double[prioritiesAmount];
        SimpleMatrix simpleMatrix = new SimpleMatrix(matrix);
        SimpleMatrix sumsOfCols = new SimpleMatrix(CommonOps.sumCols(simpleMatrix.getMatrix(), null));
        double sumOfInvertedColsSums = 0;
        double[] sumsOfColsArray = sumsOfCols.getMatrix().getData();
        for (double sumOfCol : sumsOfColsArray) {
            sumOfInvertedColsSums += 1/sumOfCol;
        }
        for(int i = 0; i < sumsOfColsArray.length; i++) {
            localPriorities[i] = (1/sumsOfColsArray[i])/sumOfInvertedColsSums;
        }
        return localPriorities;
    }

    private double[] getPrioritiesByColumnNormalization(double[][] matrix) {
        int prioritiesAmount = matrix.length;
        double[] localPriorities = new double[prioritiesAmount];
        SimpleMatrix simpleMatrix = new SimpleMatrix(matrix);
        SimpleMatrix sumsOfCols = new SimpleMatrix(CommonOps.sumCols(simpleMatrix.getMatrix(), null));
        for(int i = 0; i < simpleMatrix.numCols(); i++) {
            for(int j = 0; j < simpleMatrix.numRows(); j++) {
                simpleMatrix.set(j, i, simpleMatrix.get(j, i)/sumsOfCols.get(i));
            }
        }
        SimpleMatrix sumsOfRows = new SimpleMatrix(CommonOps.sumRows(simpleMatrix.getMatrix(), null));
        for(int i = 0; i < prioritiesAmount; i++) {
            localPriorities[i] = sumsOfRows.get(i)/simpleMatrix.numCols();
        }
        return localPriorities;
    }

    private double[] getPrioritiesByMultiplyingElements(double[][] matrix) {
        int prioritiesAmount = matrix.length;
        double[] localPriorities = new double[prioritiesAmount];
        for(int i = 0; i < prioritiesAmount; i++) {
            double product = 1;
            for (double element : matrix[i]) {
                product *= element;
            }
            localPriorities[i] = Math.pow(product, 1.0/matrix[i].length);
        }
        normalizeVector(localPriorities);
        return localPriorities;
    }

    private void normalizeVector(double[] vector) {
        double sum = 0;
        for(double element : vector) {
            sum += element;
        }
        for(int i = 0; i < vector.length; i++) {
            vector[i] /= sum;
        }
    }

    public boolean isMatrixConcerted(double[][] matrix, double[] priorities) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(matrix);
        int n = priorities.length;
        DenseMatrix64F prioritiesMatrix = DenseMatrix64F.wrap(n, 1, priorities);
        DenseMatrix64F vectorN = new DenseMatrix64F(n, 1);
        CommonOps.mult(simpleMatrix.getMatrix(), prioritiesMatrix, vectorN);

        CommonOps.elementDiv(vectorN, prioritiesMatrix);

        double lambda = CommonOps.elementSum(vectorN) / n;
        double concertnessIndex = (lambda - n)/(n - 1);

        double concertnessRelation = concertnessIndex;
        if(matrix.length > 2) {
            concertnessRelation /= AVERAGE_LEVEL_OF_MATRIX_CONCERTNESS[matrix.length - 1];
        }
        return concertnessRelation <= 0.1d;
    }

    public double[][] concertMatrix(double[][] matrix) {
        double[] priorities = getLocalPriorities(matrix);
        while(!isMatrixConcerted(matrix, priorities)) {
            int row = -1;
            double maxDeviation = 0;
            for(int i = 0; i < matrix.length; i++) {
                double sumOfDeviation = 0;
                for (int j = 0; j< matrix.length; j++) {
                    sumOfDeviation += Math.abs(matrix[i][j] - priorities[i]/priorities[j]);
                }
                if(sumOfDeviation > maxDeviation) {
                    maxDeviation = sumOfDeviation;
                    row = i;
                }
            }
            if (row < 0) {
                throw new IllegalArgumentException("Cannot concert matrix");
            }
            for (int j = 0; j < matrix.length; j++) {
                matrix[row][j] = priorities[row]/priorities[j];
            }
            priorities = getLocalPriorities(matrix);
        }
        return matrix;
    }

    public Map<Product, Double> calculateGlobalPriorities(ProductPolicy productPolicy) {
        List<AlternativeLevel> alternativeLevels = alternativeLevelRepository.findByProductPolicy(productPolicy);
        Map<Integer, List<AlternativeLevel>> levelToListAlternativesMap = alternativeLevels.stream().collect(groupingBy(AlternativeLevel::getLevel, toList()));
        int amountOfLevels = alternativeLevels.stream().mapToInt(AlternativeLevel::getLevel).max().orElse(-1);
        Map<Alternative, Double> globalPrioritiesForAlternatives = new HashMap<>();
        globalPrioritiesForAlternatives.put(alternativeLevels.stream().filter(alternativeLevel -> alternativeLevel.getLevel() == 0).findFirst().orElse(null).getId().getAlternative(), 1d);
        for(int i = 1; i < amountOfLevels; i++) {
            List<Alternative> alternatives = alternativeLevelRepository.findByProductPolicyAndLevel(productPolicy, i)
                    .stream().map(alternativeLevel -> alternativeLevel.getId().getAlternative())
                    .collect(toList());
            List<Alternative> previousLevelAlternatives = levelToListAlternativesMap.get(i - 1).stream().map(alternativeLevel -> alternativeLevel.getId().getAlternative()).collect(toList());
            double[][] localPriorities = new double[alternatives.size()][previousLevelAlternatives.size()];
            for(int j = 0; j < previousLevelAlternatives.size(); j++) {
                double[][] matrix = getCommonMatrixForAlternative(productPolicy, previousLevelAlternatives.get(j));
                double[] localPrioritiesForAlternative = getLocalPriorities(matrix);
                for(int k = 0; k < localPrioritiesForAlternative.length; k++) {
                    localPriorities[k][j] = localPrioritiesForAlternative[k];
                }
            }
            double[] globalPriorities = globalPrioritiesForAlternatives.entrySet().stream().filter(entry -> previousLevelAlternatives.contains(entry.getKey())).map(Map.Entry::getValue).mapToDouble(value -> value).toArray();
            DenseMatrix64F nextGlobalPriorities = new DenseMatrix64F(alternatives.size(), 1);
            CommonOps.mult(new SimpleMatrix(localPriorities).getMatrix(), DenseMatrix64F.wrap(globalPriorities.length, 1, globalPriorities), nextGlobalPriorities);
            for (int k = 0; k < alternatives.size(); k++) {
                globalPrioritiesForAlternatives.put(alternatives.get(k), nextGlobalPriorities.get(k));
            }
        }
        List<Integer> productIds = alternativeLevels.stream().filter(alternativeLevel -> alternativeLevel.getLevel() == amountOfLevels).map(alternativeLevel -> alternativeLevel.getId().getAlternative().getId()).collect(toList());
        List<Product> products = Lists.newArrayList(productRepository.findAll(productIds));
        Map<Product, Double> globalPrioritiesForProduct = new HashMap<>();
        List<Alternative> lastLevelAlternatives = levelToListAlternativesMap.get(amountOfLevels - 1).stream().map(alternativeLevel -> alternativeLevel.getId().getAlternative()).collect(toList());
        for (Alternative criteria : lastLevelAlternatives) {
            List<Product> productsByCriteria = products.stream().filter(product -> criteria.equals(product.getProductGroup())).collect(toList());
            double[][] matrix = getCommonMatrixForAlternative(productPolicy, criteria);
            double[] localPrioritiesForAlternative = getLocalPriorities(matrix);
            double[] globalPriorities = {globalPrioritiesForAlternatives.get(criteria)};
            DenseMatrix64F nextGlobalPriorities = new DenseMatrix64F(productsByCriteria.size(), 1);
            CommonOps.mult(DenseMatrix64F.wrap(localPrioritiesForAlternative.length, 1, localPrioritiesForAlternative), DenseMatrix64F.wrap(globalPriorities.length, 1, globalPriorities), nextGlobalPriorities);
            for (int k = 0; k < productsByCriteria.size(); k++) {
                globalPrioritiesForProduct.put(productsByCriteria.get(k), nextGlobalPriorities.get(k));
            }
        }
        return globalPrioritiesForProduct;
    }

    public Map<Integer, List<Alternative>> getHierarchy(ProductPolicy productPolicy) {
        List<AlternativeLevel> hierarchy = alternativeLevelRepository.findByProductPolicy(productPolicy);
        return hierarchy.stream()
                .collect(Collectors.groupingBy(AlternativeLevel::getLevel, Collectors.mapping(alternativeLevel -> alternativeLevel.getId().getAlternative(), Collectors.toList())));
    }

    public Pair<Alternative, List<? extends Alternative>> getNextAlternativeToComparison(ProductPolicy productPolicy, User expert) {
        List<AlternativeLevel> alternativeLevels = alternativeLevelRepository.findByProductPolicy(productPolicy);
        int levelCount = alternativeLevels.stream().mapToInt(AlternativeLevel::getLevel).max().orElseThrow(IllegalArgumentException::new);
        List<Alternative> criteriaToCompare = comparisonMatrixRepository.findAllByProductPolicyAndExpert(productPolicy, expert).stream().filter(comparisonMatrix -> isNull(comparisonMatrix.getDateCreation())).map(ComparisonMatrix::getComparisonCriteria).collect(toList());
        if(criteriaToCompare.isEmpty()) {
            return null;
        }
        AlternativeLevel alternativeLevelToCompare = alternativeLevels.stream().filter(alternativeLevel -> criteriaToCompare.contains(alternativeLevel.getId().getAlternative())).sorted(comparingInt(AlternativeLevel::getLevel)).findFirst().orElse(null);
        List<? extends Alternative> alternatives = alternativeLevelToCompare.getLevel() < levelCount - 1 ? getAlternativesForCriteria(productPolicy, alternativeLevelToCompare.getLevel() + 1) : getProductsForCriteria(alternativeLevelToCompare.getId().getAlternative());
        return Pair.of(alternativeLevelToCompare.getId().getAlternative(), alternatives);
    }

    private List<? extends Alternative> getProductsForCriteria(Alternative alternative) {
        return productRepository.findProductsByProductGroup(criteriaRepository.findOne(alternative.getId()));
    }

    private List<Alternative> getAlternativesForCriteria(ProductPolicy productPolicy, int level) {
        return alternativeLevelRepository.findByProductPolicyAndLevel(productPolicy, level).stream().map(alternativeLevel -> alternativeLevel.getId().getAlternative()).collect(toList());
    }

    @Transactional
    public void setComparisonMarks(ProductPolicy productPolicy, Criteria criteria, User expert, Map<String, Double> alternativesToMark) {
        ComparisonMatrix comparisonMatrix = comparisonMatrixRepository.findByProductPolicyAndComparisonCriteriaAndExpert(productPolicy, criteria, expert);
        List<ComparisonMatrixElement> elements = comparisonMatrixElementRepository.findAllByComparisonMatrix(comparisonMatrix);
        for(ComparisonMatrixElement element : elements) {
            int row = element.getId().getRow();
            int column = element.getId().getColumn();
            if(row == column) {
                comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, row, column, 1);
                continue;
            }
            int firstId = element.getFirst_alternative().getId();
            int secondId = element.getSecond_alternative().getId();
            String firstToSecond = StringUtils.join(Arrays.asList(firstId, secondId), "To");
            if(alternativesToMark.containsKey(firstToSecond)) {
                double value = alternativesToMark.get(firstToSecond);
                comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, row, column, value);
                comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, column, row, 1/value);
            }
//            String secondToFirst = StringUtils.join(Arrays.asList(secondId, firstId), "To");
//            if (alternativesToMark.containsKey(secondToFirst)) {
//                double value = alternativesToMark.get(secondToFirst);
//                comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, row, column, 1/value);
//                comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, column, row, value);
//            }
        }
        updateComparisonMatrixWithDateCreation(comparisonMatrix, new Date());
    }

    @Transactional
    public void updateCommonMatricesWithMarks(ProductPolicy policy) {
        List<ComparisonMatrix> allMatrixes = comparisonMatrixRepository.findAllByProductPolicy(policy).stream().filter(matrix -> nonNull(matrix.getExpert())).collect(toList());
        Map<Alternative, List<ComparisonMatrix>> alternativeToMatrixMap = allMatrixes.stream().collect(groupingBy(ComparisonMatrix::getComparisonCriteria, toList()));
        Map<Alternative, ComparisonMatrix> alternativeToCommonMatrixMap = alternativeToMatrixMap.keySet()
                .stream()
                .map(alternative -> comparisonMatrixRepository.findByProductPolicyAndComparisonCriteriaAndExpert(policy, alternative, null))
                .collect(toMap(ComparisonMatrix::getComparisonCriteria, identity()));
        for(Map.Entry<Alternative, List<ComparisonMatrix>> entry : alternativeToMatrixMap.entrySet()) {
            List<double[][]> matrices = entry.getValue().stream().map(this::getArrayOfMarks).collect(toList());
            int rowCount = matrices.get(0).length;
            int columnCount = matrices.get(0)[0].length;
            SimpleMatrix initialMatrix = new SimpleMatrix(rowCount, columnCount);
            D1Matrix64F result = new DenseMatrix64F(rowCount, columnCount);
            for(double[][] matrix : matrices) {
                SimpleMatrix currentMatrix = new SimpleMatrix(matrix);
                CommonOps.add(initialMatrix.getMatrix(), currentMatrix.getMatrix(), result);
                initialMatrix = new SimpleMatrix(result);
            }
            CommonOps.divide(matrices.size(), result);
            double[] resultData = result.getData();
            ComparisonMatrix comparisonMatrix = alternativeToCommonMatrixMap.get(entry.getKey());
            double[][] resultMatrix = new double[rowCount][columnCount];
            for(int i = 0; i < rowCount; i++) {
                for(int j = 0; j < columnCount; j++) {
                    resultMatrix[i][j] = resultData[i * columnCount + j];
                }
            }
            resultMatrix = concertMatrix(resultMatrix);
            for(int i = 0; i < rowCount; i++) {
                for(int j = 0; j < columnCount; j++) {
                    comparisonMatrixElementRepository.setValueOfMatrixElement(comparisonMatrix, i, j, resultMatrix[i][j]);
                }
            }
            updateComparisonMatrixWithDateCreation(comparisonMatrix, new Date());
        }
    }

    private double[][] getArrayOfMarks(ComparisonMatrix comparisonMatrix) {
        List<ComparisonMatrixElement> allElementsInComparisonMatrix = comparisonMatrixElementRepository.findAllByComparisonMatrix(comparisonMatrix);
        int columnCount = allElementsInComparisonMatrix.stream().mapToInt(comparisonMatrixElement -> comparisonMatrixElement.getId().getColumn()).max().orElse(-1) + 1;
        int rowCount = allElementsInComparisonMatrix.stream().mapToInt(comparisonMatrixElement -> comparisonMatrixElement.getId().getRow()).max().orElse(-1) + 1;
        double[][] matrix = new double[rowCount][columnCount];
        for(ComparisonMatrixElement element : allElementsInComparisonMatrix) {
            matrix[element.getId().getRow()][element.getId().getColumn()] = element.getValue();
        }
        return matrix;
    }

    public boolean isCommonMatricesFilled(ProductPolicy productPolicy) {
        return areAllMatricesForUserFilled(productPolicy, null);
    }
}
