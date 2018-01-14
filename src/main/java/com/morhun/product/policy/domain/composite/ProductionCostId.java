package com.morhun.product.policy.domain.composite;

import com.morhun.product.policy.domain.Material;
import com.morhun.product.policy.domain.Product;
import com.morhun.product.policy.domain.ProductPolicy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Yaroslav_Morhun
 */
@Embeddable
public class ProductionCostId implements Serializable {

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "alternative_id")
    private Product product;
    @ManyToOne(targetEntity = Material.class)
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne(targetEntity = ProductPolicy.class)
    @JoinColumn(name = "product_policy_id")
    private ProductPolicy productPolicy;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ProductPolicy getProductPolicy() {
        return productPolicy;
    }

    public void setProductPolicy(ProductPolicy productPolicy) {
        this.productPolicy = productPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductionCostId that = (ProductionCostId) o;

        return new EqualsBuilder()
                .append(product, that.product)
                .append(material, that.material)
                .append(productPolicy, that.productPolicy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(product)
                .append(material)
                .append(productPolicy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("product", product)
                .append("material", material)
                .append("product policy", productPolicy)
                .toString();
    }
}
