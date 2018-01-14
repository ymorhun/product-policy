package com.morhun.product.policy.controller;

import com.morhun.product.policy.domain.ProductPolicy;
import com.morhun.product.policy.domain.User;
import com.morhun.product.policy.dto.CreateProductPolicyDto;
import com.morhun.product.policy.dto.ProductPolicyInfoDto;
import com.morhun.product.policy.dto.UserDto;
import com.morhun.product.policy.dto.UserInfoDto;
import com.morhun.product.policy.service.ProductPolicyService;
import com.morhun.product.policy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Created by yarki on 13.06.2017.
 */
@Controller
@RequestMapping(value = "/research")
public class ResearchController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductPolicyService productPolicyService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String prepareInfoToCreateResearch(ModelMap modelMap) {
        List<UserInfoDto> personsCreateDecision = userService.findPersonsCreateDecisions().stream().map(user -> {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setId(user.getId());
            userInfoDto.setName(user.getFirst_name() + SPACE + user.getLast_name());
            return userInfoDto;
        }).collect(toList());

        modelMap.addAttribute("personsCreateDecision", personsCreateDecision);
        modelMap.addAttribute("createProductPolicyDto", new CreateProductPolicyDto());
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createResearch(@RequestParam String name, @RequestParam String description,
                                 @RequestParam int duration, @RequestParam int personCreatesDecisionId,
                                 HttpSession httpSession) {
        ProductPolicy productPolicy = new ProductPolicy();
        productPolicy.setName(name);
        productPolicy.setDescription(description);
        productPolicy.setDuration(duration);
        productPolicy.setPersonCreatesDecision(userService.getUserById(personCreatesDecisionId));
        productPolicy.setInitiator(getUserFromSession(httpSession));
        productPolicy.setDateInitiation(new Date());
        productPolicyService.createNewProductPolicy(productPolicy);
        return "policy_created";
    }

    private User getUserFromSession(HttpSession httpSession) {
        return userService.getUserById(((UserDto) httpSession.getAttribute("user")).getId());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getAllResearches(ModelMap modelMap, HttpSession httpSession) {
        User user = getUserFromSession(httpSession);
        List<ProductPolicyInfoDto> productPolicies = productPolicyService.getProductPoliciesInitiatedBy(user).stream().map(productPolicy -> {
            ProductPolicyInfoDto dto = new ProductPolicyInfoDto();
            dto.setProductPolicyId(productPolicy.getId());
            dto.setDescription(productPolicy.getDescription());
            dto.setName(productPolicy.getName());
            dto.setInitiated(productPolicy.getDateInitiation());
            dto.setDuration(productPolicy.getDuration());
            dto.setFinished(productPolicy.getDateCreation());
            return dto;
        }).collect(toList());
        modelMap.addAttribute("productPolicies", productPolicies);
        return "list";
    }

    @RequestMapping(value = "/list/new", method = RequestMethod.GET)
    public String getNewResearches(ModelMap modelMap, HttpSession httpSession) {
        User user = getUserFromSession(httpSession);
        List<ProductPolicyInfoDto> productPolicies = productPolicyService.getProductPoliciesWithPersonCreatesDecision(user)
                .stream()
                .filter(productPolicy -> !productPolicyService.isProductPolicyConfigured(productPolicy))
                .map(productPolicy -> {
                    ProductPolicyInfoDto dto = new ProductPolicyInfoDto();
                    dto.setProductPolicyId(productPolicy.getId());
                    dto.setDescription(productPolicy.getDescription());
                    dto.setName(productPolicy.getName());
                    dto.setInitiated(productPolicy.getDateInitiation());
                    dto.setDuration(productPolicy.getDuration());
                    dto.setFinished(productPolicy.getDateCreation());
                    return dto;
                }).collect(toList());
        modelMap.addAttribute("productPolicies", productPolicies);
        return "list";
    }

    @RequestMapping(value = "/{productPolicyId}", method = RequestMethod.GET)
    public String configProductPolicy(@PathVariable int productPolicyId, ModelMap modelMap) {
        ProductPolicy productPolicy = productPolicyService.getProductPolicyById(productPolicyId);
        ProductPolicyInfoDto dto = new ProductPolicyInfoDto();
        dto.setProductPolicyId(productPolicy.getId());
        dto.setDescription(productPolicy.getDescription());
        dto.setName(productPolicy.getName());
        dto.setInitiated(productPolicy.getDateInitiation());
        dto.setDuration(productPolicy.getDuration());
        modelMap.addAttribute("productPolicy", dto);
        modelMap.addAttribute("createdBy", productPolicy.getInitiator().getFirst_name() + " " + productPolicy.getInitiator().getLast_name());
        List<UserInfoDto> experts = userService.findAvailableExperts().stream().map(user -> { UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setId(user.getId());
            userInfoDto.setName(user.getFirst_name() + " " + user.getLast_name());
            return userInfoDto;}).collect(toList());
        modelMap.addAttribute("experts", experts);
        return "assortment";
    }
}
