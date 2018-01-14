package com.morhun.product.policy.dto;

/**
 * @author Yaroslav_Morhun
 */
public class MaterialDto {
    private int id;
    private String name;
    private String units;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
