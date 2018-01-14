package com.morhun.product.policy.service;

import com.google.common.collect.Lists;
import com.morhun.product.policy.domain.*;
import com.morhun.product.policy.domain.composite.ProductPolicyMaterialId;
import com.morhun.product.policy.domain.composite.ProductionCostId;
import com.morhun.product.policy.domain.composite.ValuesFabricId;
import com.morhun.product.policy.linear.SimplexSubTask;
import com.morhun.product.policy.linear.SimplexTask;
import com.morhun.product.policy.repository.MaterialRepository;
import com.morhun.product.policy.repository.ProductPolicyMaterialRepository;
import com.morhun.product.policy.repository.ProductionCostRepository;
import com.morhun.product.policy.repository.ValuesFabricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by yarki on 25.06.2017.
 */
@Service
public class ProductionService {
    @Autowired
    private ValuesFabricRepository valuesFabricRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProductPolicyMaterialRepository productPolicyMaterialRepository;

    @Autowired
    private ProductionCostRepository productionCostRepository;

    public void storeProductsToProduction(List<Product> products, ProductPolicy productPolicy) {
        for(Product product : products) {
            ValuesFabric valuesFabric = new ValuesFabric();
            ValuesFabricId id = new ValuesFabricId();
            id.setProduct(product);
            id.setProductPolicy(productPolicy);
            valuesFabric.setId(id);
            valuesFabric.setPrice(-1d);
            valuesFabric.setVolume(-1d);
            valuesFabricRepository.save(valuesFabric);
        }
    }

    @Transactional
    public void updateProductsWithPrices(ProductPolicy productPolicy, Map<Product, Double> productPrices) {
        for(Map.Entry<Product, Double> pp : productPrices.entrySet()) {
            valuesFabricRepository.setPriceForProduct(pp.getKey(), productPolicy, pp.getValue());
        }
    }

    public List<Material> getMaterials(){
        return Lists.newArrayList(materialRepository.findAll());
    }

    public Material getMaterial(int id) {
        return materialRepository.findOne(id);
    }

    public void createMaterialsWithVolumes(ProductPolicy productPolicy, Map<Material, Double> materialVolumes) {
        for(Map.Entry<Material, Double> materialVolume : materialVolumes.entrySet()) {
            ProductPolicyMaterial productPolicyMaterial = new ProductPolicyMaterial();
            ProductPolicyMaterialId productPolicyMaterialId = new ProductPolicyMaterialId();
            productPolicyMaterialId.setMaterial(materialVolume.getKey());
            productPolicyMaterialId.setProductPolicy(productPolicy);
            productPolicyMaterial.setId(productPolicyMaterialId);
            productPolicyMaterial.setVolume(materialVolume.getValue());
            productPolicyMaterialRepository.save(productPolicyMaterial);
        }
    }

    public List<Material> getMaterialsForProductPolicy(ProductPolicy productPolicy) {
        return productPolicyMaterialRepository.findAllByProductPolicy(productPolicy).stream().map(productPolicyMaterial -> productPolicyMaterial.getId().getMaterial()).sorted(Comparator.comparingInt(Material::getId)).collect(toList());
    }

    public List<Product> getProductsForProductPolicy(ProductPolicy productPolicy) {
        return valuesFabricRepository.findAllByProductPolicy(productPolicy).stream().map(valuesFabric -> valuesFabric.getId().getProduct()).sorted(Comparator.comparingInt(Alternative::getId)).collect(toList());
    }

    public void saveProductionCost(ProductPolicy productPolicy, Product product, Material material, double cost) {
        ProductionCost productionCost = new ProductionCost();
        ProductionCostId productionCostId = new ProductionCostId();
        productionCostId.setMaterial(material);
        productionCostId.setProduct(product);
        productionCostId.setProductPolicy(productPolicy);
        productionCost.setId(productionCostId);
        productionCost.setVolumePerPiece(cost);
        productionCostRepository.save(productionCost);
    }

    public List<ValuesFabric> getProductPricesForProductPolicies(ProductPolicy productPolicy) {
        return valuesFabricRepository.findAllByProductPolicy(productPolicy);
    }

    public List<ProductionCost> getExpensesForProductPolicy(ProductPolicy productPolicy) {
        return productionCostRepository.findAllByProductPolicy(productPolicy);
    }

    public List<ProductionCost> getExpensesForProductPolicyAndMaterial(ProductPolicy productPolicy, Material material) {
        return productionCostRepository.findAllByProductPolicyAndMaterial(productPolicy, material);
    }

    public double getMaterialReserve(ProductPolicy productPolicy, Material material) {
        ProductPolicyMaterialId productPolicyMaterialId = new ProductPolicyMaterialId();
        productPolicyMaterialId.setProductPolicy(productPolicy);
        productPolicyMaterialId.setMaterial(material);
        return productPolicyMaterialRepository.findOne(productPolicyMaterialId).getVolume();
    }

    @Transactional
    public List<ValuesFabric> calculateProductionVolumes(ProductPolicy productPolicy) {
        List<Product> products = getProductsForProductPolicy(productPolicy);
        List<Material> materials = getMaterialsForProductPolicy(productPolicy);
        Map<Product, Double> productPrices = getProductPricesForProductPolicies(productPolicy).stream().collect(toMap(o -> o.getId().getProduct(), ValuesFabric::getPrice));
        List<ProductionCost> productionCosts = getExpensesForProductPolicy(productPolicy);
        double[] prices = new double[products.size()];
        double[][] expenses = new double[materials.size()][products.size()];
        double[] volumes = new double[materials.size()];
        int productNumber = 0;
        for(Product product : products) {
            prices[productNumber] = productPrices.get(product);
            Map<Material, Double> expensesByProduct = productionCosts.stream().filter(productionCost -> productionCost.getId().getProduct().equals(product)).collect(toMap(productionCost -> productionCost.getId().getMaterial(), ProductionCost::getVolumePerPiece));
            int materialNumber = 0;
            for(Material material : materials) {
                expenses[materialNumber][productNumber] = expensesByProduct.get(material);
                materialNumber++;
            }
            productNumber++;
        }
        Map<Material, Double> materialVolumes = productPolicyMaterialRepository.findAllByProductPolicy(productPolicy).stream().collect(toMap(productPolicyMaterial -> productPolicyMaterial.getId().getMaterial(), ProductPolicyMaterial::getVolume));
        int materialNumber = 0;
        for(Material material : materials) {
            volumes[materialNumber] = materialVolumes.get(material);
            materialNumber++;
        }
        SimplexSubTask simplexSubTask = new SimplexSubTask(prices, volumes, expenses, products.toArray(new Product[products.size()]));
        double[] firstStepSolution = simplexSubTask.calculateVolumes();
        int[] basis = new int[volumes.length];
        int basisIndex = 0;
        for(int index=0; index < firstStepSolution.length; index++) {
            if(firstStepSolution[index] > 0) {
                if(index >= prices.length) {
                    throw new IllegalArgumentException();
                }
                basis[basisIndex] = index;
                basisIndex++;
            }
        }
        SimplexTask simplexTask = new SimplexTask(prices, volumes, expenses, products.toArray(new Product[products.size()]), basis);
        double[] solution = simplexTask.calculateVolumesOfProducts();
        List<ValuesFabric> valuesFabricList = valuesFabricRepository.findAllByProductPolicy(productPolicy);
        for(int i = 0; i< solution.length; i++) {
            int indexOfProduct = i;
            valuesFabricList.stream()
                    .filter(valuesFabric -> valuesFabric.getId().getProduct().equals(products.get(indexOfProduct)))
                    .findFirst()
                    .ifPresent(valuesFabric -> valuesFabric.setVolume(solution[indexOfProduct]));
        }
        valuesFabricRepository.save(valuesFabricList);
        return valuesFabricList;


    }
}
