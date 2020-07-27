package hu.xiaoping.bestshop.product.repository;

import hu.xiaoping.bestshop.product.domain.ProductBundle;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProductBundle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long> {

    @Query(value = "SELECT pb FROM ProductBundle pb where exists (select 1 from ProductBundleItem pbi WHERE pbi.productBundle.id = pb.id and pbi.product.id=:productId)")
    List<ProductBundle> findAllByProductId(@Param("productId")  Long productId);
}
