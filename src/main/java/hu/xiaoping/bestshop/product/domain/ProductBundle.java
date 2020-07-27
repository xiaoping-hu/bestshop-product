package hu.xiaoping.bestshop.product.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProductBundle.
 */
@Entity
@Table(name = "product_bundle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @OneToMany(mappedBy = "productBundle", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductBundleItem> productBundleItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProductBundle name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProductBundleItem> getProductBundleItems() {
        return productBundleItems;
    }

    public ProductBundle productBundleItems(Set<ProductBundleItem> productBundleItems) {
        this.productBundleItems = productBundleItems;
        return this;
    }

    public ProductBundle addProductBundleItems(ProductBundleItem productBundleItem) {
        this.productBundleItems.add(productBundleItem);
        productBundleItem.setProductBundle(this);
        return this;
    }

    public ProductBundle removeProductBundleItems(ProductBundleItem productBundleItem) {
        this.productBundleItems.remove(productBundleItem);
        productBundleItem.setProductBundle(null);
        return this;
    }

    public void setProductBundleItems(Set<ProductBundleItem> productBundleItems) {
        this.productBundleItems = productBundleItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductBundle)) {
            return false;
        }
        return id != null && id.equals(((ProductBundle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductBundle{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
