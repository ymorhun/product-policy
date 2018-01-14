package com.morhun.product.policy.dto;

import java.util.List;

/**
 * Created by yarki on 12.06.2017.
 */
public class UserDto {
    private int id;
    private String login;
    private List<String> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
