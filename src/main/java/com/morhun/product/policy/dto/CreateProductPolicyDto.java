package com.morhun.product.policy.dto;

/**
 * Created by yarki on 17.10.2017.
 */
public class CreateProductPolicyDto {
    private String name;
    private String description;
    private int duration;
    private int personCreatesDecisionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPersonCreatesDecisionId() {
        return personCreatesDecisionId;
    }

    public void setPersonCreatesDecisionId(int personCreatesDecisionId) {
        this.personCreatesDecisionId = personCreatesDecisionId;
    }
}
