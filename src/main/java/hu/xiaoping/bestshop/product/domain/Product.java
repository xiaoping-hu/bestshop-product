package hu.xiaoping.bestshop.product.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition="clob")
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductBundleItem> productBundleItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Product title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<ProductBundleItem> getProductBundleItems() {
        return productBundleItems;
    }

    public Product productBundleItems(Set<ProductBundleItem> productBundleItems) {
        this.productBundleItems = productBundleItems;
        return this;
    }

    public Product addProductBundleItems(ProductBundleItem productBundleItem) {
        this.productBundleItems.add(productBundleItem);
        productBundleItem.setProduct(this);
        return this;
    }

    public Product removeProductBundleItems(ProductBundleItem productBundleItem) {
        this.productBundleItems.remove(productBundleItem);
        productBundleItem.setProduct(null);
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
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
