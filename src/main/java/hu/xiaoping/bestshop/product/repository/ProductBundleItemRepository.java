package hu.xiaoping.bestshop.product.repository;

import hu.xiaoping.bestshop.product.domain.ProductBundleItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProductBundleItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductBundleItemRepository extends JpaRepository<ProductBundleItem, Long> {
}
