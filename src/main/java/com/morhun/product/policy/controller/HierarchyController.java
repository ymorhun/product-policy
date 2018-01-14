package com.morhun.product.policy.controller;

import com.morhun.product.policy.domain.*;
import com.morhun.product.policy.dto.ProductPolicyInfoDto;
import com.morhun.product.policy.dto.ProductPriorityDto;
import com.morhun.product.policy.dto.UserDto;
import com.morhun.product.policy.dto.UserInfoDto;
import com.morhun.product.policy.service.AlternativeService;
import com.morhun.product.policy.service.HierarchyService;
import com.morhun.product.policy.service.ProductPolicyService;
import com.morhun.product.policy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author Yaroslav_Morhun
 */
@Controller
@RequestMapping("/hierarchy")
public class HierarchyController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductPolicyService productPolicyService;

    @Autowired
    private AlternativeService alternativeService;

    @Autowired
    private HierarchyService hierarchyService;

    @RequestMapping(value = "/assortment", method = RequestMethod.POST)
    public String createHierarchy(ModelMap modelMap, HttpServletRequest httpRequest) {
        int levels = Integer.valueOf(httpRequest.getParameter("levels"));
        Map<Criteria, Integer> criteriaIntegerMap = new HashMap<>();
        List<Criteria> productGroups = new ArrayList<>();
        for(int i = 1; i <= levels; i++) {
            int level = i-1;
            List<Criteria> criteria = Arrays.stream(httpRequest.getParameterValues("level_" + i)).map(criteriaTitle -> {
                Criteria c = new Criteria();
                c.setTitle(criteriaTitle);
                return c;
            }).collect(toList());
            criteria.forEach(c -> alternativeService.addCriteria(c));
            criteriaIntegerMap.putAll(criteria.stream().collect(toMap(Function.identity(), c -> level)));
            if (i == levels) {
                productGroups.addAll(criteria);
            }
        }
        String[] expertIds = httpRequest.getParameterValues("experts");
        List<User> experts = Arrays.stream(expertIds).map(expertId -> userService.getUserById(Integer.valueOf(expertId))).collect(toList());
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(httpRequest.getParameter("productPolicyId")));
        productPolicyService.updateWithListOfExperts(productPolicy, experts.stream().collect(toMap(Function.identity(), expert -> 1)));
        hierarchyService.createNewHierarchyForProductPolicy(productPolicy, criteriaIntegerMap);
        modelMap.addAttribute("productGroups", productGroups);
        modelMap.addAttribute("productPolicyId", productPolicy.getId());
        return "assortmentProducts";
    }

    @RequestMapping(value = "/assortment/products", method = RequestMethod.POST)
    public String addProductsToHierarchy(ModelMap modelMap, HttpServletRequest httpServletRequest) {
        Map<Integer, String[]> criteriaIdToProductNames = httpServletRequest.getParameterMap().entrySet()
                .stream()
                .filter(entry -> StringUtils.startsWith(entry.getKey(), "criteria_"))
                .collect(toMap(entry -> Integer.valueOf(StringUtils.removeStart(entry.getKey(), "criteria_")), Map.Entry::getValue));
        List<Product> allProducts = new ArrayList<>();
        for(int criteriaId : criteriaIdToProductNames.keySet()) {
            Criteria criteria = alternativeService.getCriteriaById(criteriaId);
            List<Product> products = Arrays.stream(criteriaIdToProductNames.get(criteriaId)).map(productName -> {
                Product product = new Product();
                product.setTitle(productName);
                product.setProductGroup(criteria);
                return product;
            }).collect(toList());
            products.forEach(alternativeService::addProduct);
            allProducts.addAll(products);
        }
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(httpServletRequest.getParameter("productPolicyId")));
        hierarchyService.updateHierarchyWithProducts(productPolicy, allProducts);
        hierarchyService.createComparisonMatrixForProductPolicy(productPolicy);
        Map<Integer, List<Alternative>> hierarchy = hierarchyService.getHierarchy(productPolicy);
        modelMap.addAttribute("hierarchy", hierarchy);
        return "main";
    }

    @RequestMapping(value = "/comparison", method = RequestMethod.GET)
    public String listEmptyComparisons(ModelMap modelMap, HttpSession httpSession) {
        modelMap.addAttribute("productPolicies", productPolicyService.getProductPoliciesWithEmptyComparisonsForExpert(getUserFromSession(httpSession)).stream().map(productPolicy -> {
            ProductPolicyInfoDto dto = new ProductPolicyInfoDto();
            dto.setProductPolicyId(productPolicy.getId());
            dto.setDescription(productPolicy.getDescription());
            dto.setName(productPolicy.getName());
            dto.setInitiated(productPolicy.getDateInitiation());
            dto.setDuration(productPolicy.getDuration());
            dto.setFinished(productPolicy.getDateCreation());
            return dto;
        }).collect(toList()));
        return "listComparison";
    }

    @RequestMapping(value = "/compare/{policyId}", method = RequestMethod.GET)
    public String doCompare(@PathVariable int policyId, HttpSession httpSession, ModelMap modelMap) {
        modelMap.addAttribute("productPolicyId", policyId);
        ProductPolicy productPolicyById = productPolicyService.getProductPolicyById(policyId);
        User expert = getUserFromSession(httpSession);
        Pair<Alternative, List<? extends Alternative>> nextAlternativeToComparison = hierarchyService.getNextAlternativeToComparison(productPolicyById, expert);
        if (isNull(nextAlternativeToComparison)) {
            return "comparisonDone";
        }
        modelMap.addAttribute("criteria", nextAlternativeToComparison.getLeft());
        modelMap.addAttribute("alternatives", nextAlternativeToComparison.getRight());
        return "compare";
    }

    @RequestMapping(value = "/compare", method = RequestMethod.POST)
    public String compareAnalyze(HttpServletRequest request, HttpSession session, ModelMap modelMap) {
        List<String> radioButtonNames = Collections.list(request.getParameterNames()).stream().filter(name -> StringUtils.contains(name, "To")).collect(toList());
        Criteria criteria = alternativeService.getCriteriaById(Integer.valueOf(request.getParameter("criteriaId")));
        User expert = getUserFromSession(session);
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(Integer.valueOf(request.getParameter("productPolicyId")));
        hierarchyService.setComparisonMarks(productPolicy, criteria, expert, radioButtonNames.stream().collect(toMap(Function.identity(), name -> Double.valueOf(request.getParameter(name)))));
        modelMap.addAttribute("productPolicyId", productPolicy.getId());
        return doCompare(productPolicy.getId(), session, modelMap);
    }

    private User getUserFromSession(HttpSession httpSession) {
        return userService.getUserById(((UserDto) httpSession.getAttribute("user")).getId());
    }

    @RequestMapping(value="/comparison/done", method = RequestMethod.GET)
    public String showDoneComparisons(HttpSession session, ModelMap modelMap) {
        List<ProductPolicy> productPolicies = productPolicyService.getProductPoliciesWithPersonCreatesDecision(getUserFromSession(session));
        List<ProductPolicy> productPoliciesReadyToAssortment = productPolicies.stream().filter(productPolicy -> hierarchyService.areAllMatricesFilled(productPolicy)).collect(toList());
        productPoliciesReadyToAssortment.stream()
                .filter(productPolicy -> !hierarchyService.isCommonMatricesFilled(productPolicy))
                .forEach(productPolicy -> hierarchyService.updateCommonMatricesWithMarks(productPolicy));
        modelMap.addAttribute("productPolicies", productPoliciesReadyToAssortment);
        return "listComparedProductPolicies";
    }

    @RequestMapping(value = "/assortment/{policyId}", method = RequestMethod.GET)
    public String showPrioritiesForProductPolicy(@PathVariable int policyId, ModelMap modelMap) {
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(policyId);
        List<ProductPriorityDto> productPriorities = hierarchyService.calculateGlobalPriorities(productPolicy).entrySet().stream().map(entry -> {
            ProductPriorityDto dto = new ProductPriorityDto();
            dto.setName(entry.getKey().getTitle());
            dto.setProductId(entry.getKey().getId());
            dto.setPriority(entry.getValue());
            return dto;
        }).sorted(Comparator.comparingDouble(ProductPriorityDto::getPriority).reversed()).collect(toList());
        modelMap.addAttribute("productPriorities", productPriorities);
        modelMap.addAttribute("policyId", policyId);
        return "productPriorities";
    }
}
