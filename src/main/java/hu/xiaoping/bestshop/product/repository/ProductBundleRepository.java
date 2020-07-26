package hu.xiaoping.bestshop.product.repository;

import hu.xiaoping.bestshop.product.domain.ProductBundle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProductBundle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long> {
}
