package hu.xiaoping.bestshop.product.service;

import hu.xiaoping.bestshop.product.domain.ProductBundle;
import hu.xiaoping.bestshop.product.repository.ProductBundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductBundle}.
 */
@Service
@Transactional
public class ProductBundleService {

    private final Logger log = LoggerFactory.getLogger(ProductBundleService.class);

    private final ProductBundleRepository productBundleRepository;

    public ProductBundleService(ProductBundleRepository productBundleRepository) {
        this.productBundleRepository = productBundleRepository;
    }

    /**
     * Save a productBundle.
     *
     * @param productBundle the entity to save.
     * @return the persisted entity.
     */
    public ProductBundle save(ProductBundle productBundle) {
        log.debug("Request to save ProductBundle : {}", productBundle);
        return productBundleRepository.save(productBundle);
    }

    /**
     * Get all the productBundles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductBundle> findAll(Pageable pageable) {
        log.debug("Request to get all ProductBundles");
        return productBundleRepository.findAll(pageable);
    }


    /**
     * Get one productBundle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductBundle> findOne(Long id) {
        log.debug("Request to get ProductBundle : {}", id);
        return productBundleRepository.findById(id);
    }

    /**
     * Delete the productBundle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductBundle : {}", id);
        productBundleRepository.deleteById(id);
    }

    /**
     * Query all product bundles contains given product.
     *
     * @param productId id of product
     * @return the list of product bundles
     */
    @Transactional(readOnly = true)
    public List<ProductBundle> findProductBundlesByProduct(@PathVariable Long productId){
        log.debug("Request to get all ProductBundles by product id");
        return productBundleRepository.findAllByProductId(productId);
    }
}
