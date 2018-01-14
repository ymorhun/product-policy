package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.Material;
import com.morhun.product.policy.domain.ProductPolicy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class ProductPolicyMaterialId implements Serializable {
    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;
    @ManyToOne(targetEntity = Material.class)
    @JoinColumn(name = "material_id")
    private Material material;

    public ProductPolicy getProductPolicy() {
        return productPolicy;
    }

    public void setProductPolicy(ProductPolicy productPolicy) {
        this.productPolicy = productPolicy;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPolicyMaterialId that = (ProductPolicyMaterialId) o;

        return new EqualsBuilder()
                .append(productPolicy, that.productPolicy)
                .append(material, that.material)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(productPolicy)
                .append(material)
                .toHashCode();
    }
}
