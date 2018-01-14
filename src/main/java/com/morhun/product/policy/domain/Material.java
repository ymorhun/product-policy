package com.morhun.product.policy.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Entity
public class Material implements Serializable {
    @Id
    @Column(name = "material_id")
    private int id;
    private String title;
    @ManyToOne(targetEntity = Unit.class)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
