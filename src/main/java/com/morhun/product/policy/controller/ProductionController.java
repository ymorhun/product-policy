package com.morhun.product.policy.controller;

import com.morhun.product.policy.LimitMismatchException;
import com.morhun.product.policy.domain.*;
import com.morhun.product.policy.domain.composite.ValuesFabricId;
import com.morhun.product.policy.dto.ExpensesDto;
import com.morhun.product.policy.dto.MaterialDto;
import com.morhun.product.policy.dto.ProductPriceDto;
import com.morhun.product.policy.service.AlternativeService;
import com.morhun.product.policy.service.ProductPolicyService;
import com.morhun.product.policy.service.ProductionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by yarki on 25.06.2017.
 */
@Controller
@RequestMapping(value = "/production")
public class ProductionController {

    @Autowired
    private ProductPolicyService productPolicyService;
    @Autowired
    private AlternativeService alternativeService;
    @Autowired
    private ProductionService productionService;

    @RequestMapping(value = "/assortment/{policyId}/save", method = RequestMethod.POST)
    public String createAssortment(@PathVariable int policyId, HttpServletRequest request, ModelMap modelMap) {
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(policyId);
        String[] productIds = request.getParameterValues("productId");
        List<Product> products = Stream.of(productIds).map(id -> alternativeService.getProductById(Integer.valueOf(id))).sorted(Comparator.comparing(Alternative::getTitle)).collect(toList());
        productionService.storeProductsToProduction(products, productPolicy);
        modelMap.addAttribute("productPolicyId", productPolicy.getId());
        modelMap.addAttribute("products", products);
        return "priceForProducts";
    }

    @RequestMapping(value = "/assortment/price", method = RequestMethod.POST)
    public String storePrices(HttpServletRequest request, ModelMap modelMap) {
        List<String> productPriceParameterNames = Collections.list(request.getParameterNames()).stream().filter(id -> StringUtils.startsWith(id, "product_")).collect(toList());
        List<String> productIds = productPriceParameterNames.stream().map(id -> StringUtils.removeStart(id, "product_")).collect(toList());
        List<Product> products = productIds.stream().map(id -> alternativeService.getProductById(Integer.valueOf(id))).collect(toList());
        Map<Product, Double> productPriceMap = products.stream().collect(toMap(Function.identity(), product -> Double.valueOf(request.getParameter("product_" + product.getId()))));
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(request.getParameter("policyId")));
        productionService.updateProductsWithPrices(productPolicy, productPriceMap);
        modelMap.addAttribute("policyId", productPolicy.getId());
        List<Material> materials = productionService.getMaterials();
        List< MaterialDto > materialDtos = materials.stream().map(material -> {
            MaterialDto dto = new MaterialDto();
            dto.setId(material.getId());
            dto.setName(material.getTitle());
            dto.setUnits(material.getUnit().getTitle());
            return dto;
        }).collect(toList());
        modelMap.addAttribute("materials", materialDtos);
        return "chooseConstraints";
    }

    @RequestMapping(value = "/constraints", method = RequestMethod.POST)
    public String storeConstraints(HttpServletRequest httpServletRequest, ModelMap modelMap) {
        List<String> materialIds = Arrays.stream(httpServletRequest.getParameterValues("material")).collect(toList());
        Map<Material, Double> materialVolumes = materialIds.stream().collect(toMap(id -> productionService.getMaterial(Integer.valueOf(id)), id -> Double.valueOf(httpServletRequest.getParameter(id))));
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(httpServletRequest.getParameter("policyId")));
        productionService.createMaterialsWithVolumes(productPolicy, materialVolumes);
        List<Material> materials = productionService.getMaterialsForProductPolicy(productPolicy);
        List<Product> products = productionService.getProductsForProductPolicy(productPolicy);
        Map<Material, List<Product>> materialsToProductsMap = new HashMap<>();
        for(Material material : materials) {
            materialsToProductsMap.put(material, products);
        }
        modelMap.addAttribute("policyId", productPolicy.getId());
        modelMap.addAttribute("materialsToProductsMap", materialsToProductsMap);
        modelMap.addAttribute("products", products);
        return "materialsPerProduct";
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.POST)
    public String storeProductionCosts(HttpServletRequest request, ModelMap modelMap) {
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(request.getParameter("policyId")));
        List<Product> products = productionService.getProductsForProductPolicy(productPolicy);
        List<Material> materials = productionService.getMaterialsForProductPolicy(productPolicy);
        for(Product product : products) {
            for(Material material : materials) {
                double cost = Double.valueOf(request.getParameter(product.getId() + "To" + material.getId()));
                productionService.saveProductionCost(productPolicy, product, material, cost);
            }
        }
        List<ProductPriceDto> productPrices = productionService.getProductPricesForProductPolicies(productPolicy).stream()
                .sorted(Comparator.comparingInt(valuesFabric -> valuesFabric.getId().getProduct().getId()))
                .map(valuesFabric -> {
                    ProductPriceDto productPriceDto = new ProductPriceDto();
                    productPriceDto.setPrice(valuesFabric.getPrice());
                    productPriceDto.setProductId(valuesFabric.getId().getProduct().getId());
                    return productPriceDto;
                }).collect(toList());
        modelMap.addAttribute("productPrices", productPrices);
        List<ExpensesDto> expenses = new ArrayList<>();
        for(Material material : materials) {
            ExpensesDto expensesDto = new ExpensesDto();
            expensesDto.setReserve(productionService.getMaterialReserve(productPolicy, material));
            List<ProductionCost> productionCosts = productionService.getExpensesForProductPolicyAndMaterial(productPolicy, material);
            expensesDto.setProductIdToExpenses(productionCosts.stream().collect(toMap(productionCost -> productionCost.getId().getProduct().getId(), ProductionCost::getVolumePerPiece)));
            expenses.add(expensesDto);
        }
        modelMap.addAttribute("expenses", expenses);
        modelMap.addAttribute("policyId", productPolicy.getId());
        return "verifyTask";
    }

    @RequestMapping(value = "/submit/{policyId}", method = RequestMethod.POST)
    public String submit(@PathVariable int policyId, ModelMap modelMap) {
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(policyId);
        List<ValuesFabric> materialVolumes = productionService.calculateProductionVolumes(productPolicy)
                .stream()
                .sorted(Comparator.comparingDouble(ValuesFabric::getVolume))
                .collect(toList());
        productPolicyService.updateWithDateCreation(productPolicy);
//        Product product1 = new Product();
//        product1.setTitle("Продукт1");
//        Product product2 = new Product();
//        product2.setTitle("Продукт2");
//        Product product3 = new Product();
//        product3.setTitle("Продукт_3");
//        Product product4 = new Product();
//        product4.setTitle("Продукт7");
//        ValuesFabricId id1 = new ValuesFabricId();
//        id1.setProductPolicy(productPolicy);
//        id1.setProduct(product1);
//        ValuesFabric product1Fabric = new ValuesFabric();
//        product1Fabric.setId(id1);
//        product1Fabric.setVolume(4.5);
//        product1Fabric.setPrice(11);
//
//        ValuesFabricId id2 = new ValuesFabricId();
//        id2.setProductPolicy(productPolicy);
//        id2.setProduct(product2);
//        ValuesFabric product2Fabric = new ValuesFabric();
//        product2Fabric.setId(id2);
//        product2Fabric.setVolume(5);
//        product2Fabric.setPrice(12.5);
//
//        ValuesFabricId id3 = new ValuesFabricId();
//        id3.setProductPolicy(productPolicy);
//        id3.setProduct(product3);
//        ValuesFabric product3Fabric = new ValuesFabric();
//        product3Fabric.setId(id3);
//        product3Fabric.setVolume(0);
//        product3Fabric.setPrice(1);
//
//        ValuesFabricId id4 = new ValuesFabricId();
//        id4.setProductPolicy(productPolicy);
//        id4.setProduct(product4);
//        ValuesFabric product4Fabric = new ValuesFabric();
//        product4Fabric.setId(id4);
//        product4Fabric.setVolume(0);
//        product4Fabric.setPrice(4);
//        List<ValuesFabric> materialVolumes = Arrays.asList(product1Fabric, product2Fabric, product3Fabric, product4Fabric);
        modelMap.addAttribute("materialVolumes", materialVolumes);
        modelMap.addAttribute("productPolicy", productPolicy);
        return "results";
    }

    @ExceptionHandler(LimitMismatchException.class)
    public String limitMissmatchExceptionHandler() {
        return "limitsMismatch";
    }
}
