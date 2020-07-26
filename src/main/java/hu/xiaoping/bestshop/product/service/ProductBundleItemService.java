package hu.xiaoping.bestshop.product.service;

import hu.xiaoping.bestshop.product.domain.ProductBundleItem;
import hu.xiaoping.bestshop.product.repository.ProductBundleItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductBundleItem}.
 */
@Service
@Transactional
public class ProductBundleItemService {

    private final Logger log = LoggerFactory.getLogger(ProductBundleItemService.class);

    private final ProductBundleItemRepository productBundleItemRepository;

    public ProductBundleItemService(ProductBundleItemRepository productBundleItemRepository) {
        this.productBundleItemRepository = productBundleItemRepository;
    }

    /**
     * Save a productBundleItem.
     *
     * @param productBundleItem the entity to save.
     * @return the persisted entity.
     */
    public ProductBundleItem save(ProductBundleItem productBundleItem) {
        log.debug("Request to save ProductBundleItem : {}", productBundleItem);
        return productBundleItemRepository.save(productBundleItem);
    }

    /**
     * Get all the productBundleItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductBundleItem> findAll(Pageable pageable) {
        log.debug("Request to get all ProductBundleItems");
        return productBundleItemRepository.findAll(pageable);
    }


    /**
     * Get one productBundleItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductBundleItem> findOne(Long id) {
        log.debug("Request to get ProductBundleItem : {}", id);
        return productBundleItemRepository.findById(id);
    }

    /**
     * Delete the productBundleItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductBundleItem : {}", id);
        productBundleItemRepository.deleteById(id);
    }
}
