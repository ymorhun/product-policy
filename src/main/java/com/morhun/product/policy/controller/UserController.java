package com.morhun.product.policy.controller;

import com.morhun.product.policy.domain.Role;
import com.morhun.product.policy.domain.User;
import com.morhun.product.policy.dto.UserDto;
import com.morhun.product.policy.dto.UserLoginDto;
import com.morhun.product.policy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Yaroslav_Morhun
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String mainPage(HttpSession httpSession, Model model) {
        Object userObject = httpSession.getAttribute("user");
        if(nonNull(userObject)) {
            return "main";
        }
        model.addAttribute("user", new UserLoginDto());
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(UserLoginDto userLoginDto, HttpSession httpSession) {
        User user = userService.getUserByCredentials(userLoginDto.getLogin(), userLoginDto.getPassword());
        if(isNull(user)) {
            return "login";
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map(Role::getTitle).collect(Collectors.toList()));
        httpSession.setAttribute("user", userDto);
        return "main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "login";
    }
}
