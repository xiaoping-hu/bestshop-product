package hu.xiaoping.bestshop.product.web.rest;

import hu.xiaoping.bestshop.product.domain.ProductBundleItem;
import hu.xiaoping.bestshop.product.service.ProductBundleItemService;
import hu.xiaoping.bestshop.product.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link hu.xiaoping.bestshop.product.domain.ProductBundleItem}.
 */
@RestController
@RequestMapping("/api")
public class ProductBundleItemResource {

    private final Logger log = LoggerFactory.getLogger(ProductBundleItemResource.class);

    private static final String ENTITY_NAME = "productProductBundleItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductBundleItemService productBundleItemService;

    public ProductBundleItemResource(ProductBundleItemService productBundleItemService) {
        this.productBundleItemService = productBundleItemService;
    }

    /**
     * {@code POST  /product-bundle-items} : Create a new productBundleItem.
     *
     * @param productBundleItem the productBundleItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productBundleItem, or with status {@code 400 (Bad Request)} if the productBundleItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-bundle-items")
    public ResponseEntity<ProductBundleItem> createProductBundleItem(@Valid @RequestBody ProductBundleItem productBundleItem) throws URISyntaxException {
        log.debug("REST request to save ProductBundleItem : {}", productBundleItem);
        if (productBundleItem.getId() != null) {
            throw new BadRequestAlertException("A new productBundleItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductBundleItem result = productBundleItemService.save(productBundleItem);
        return ResponseEntity.created(new URI("/api/product-bundle-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-bundle-items} : Updates an existing productBundleItem.
     *
     * @param productBundleItem the productBundleItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBundleItem,
     * or with status {@code 400 (Bad Request)} if the productBundleItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productBundleItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-bundle-items")
    public ResponseEntity<ProductBundleItem> updateProductBundleItem(@Valid @RequestBody ProductBundleItem productBundleItem) throws URISyntaxException {
        log.debug("REST request to update ProductBundleItem : {}", productBundleItem);
        if (productBundleItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductBundleItem result = productBundleItemService.save(productBundleItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBundleItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-bundle-items} : get all the productBundleItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productBundleItems in body.
     */
    @GetMapping("/product-bundle-items")
    public ResponseEntity<List<ProductBundleItem>> getAllProductBundleItems(Pageable pageable) {
        log.debug("REST request to get a page of ProductBundleItems");
        Page<ProductBundleItem> page = productBundleItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-bundle-items/:id} : get the "id" productBundleItem.
     *
     * @param id the id of the productBundleItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productBundleItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-bundle-items/{id}")
    public ResponseEntity<ProductBundleItem> getProductBundleItem(@PathVariable Long id) {
        log.debug("REST request to get ProductBundleItem : {}", id);
        Optional<ProductBundleItem> productBundleItem = productBundleItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productBundleItem);
    }

    /**
     * {@code DELETE  /product-bundle-items/:id} : delete the "id" productBundleItem.
     *
     * @param id the id of the productBundleItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-bundle-items/{id}")
    public ResponseEntity<Void> deleteProductBundleItem(@PathVariable Long id) {
        log.debug("REST request to delete ProductBundleItem : {}", id);
        productBundleItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
