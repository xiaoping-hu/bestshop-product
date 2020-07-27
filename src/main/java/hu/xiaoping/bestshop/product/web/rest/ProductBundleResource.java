package hu.xiaoping.bestshop.product.web.rest;

import hu.xiaoping.bestshop.product.domain.ProductBundle;
import hu.xiaoping.bestshop.product.service.ProductBundleService;
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
 * REST controller for managing {@link hu.xiaoping.bestshop.product.domain.ProductBundle}.
 */
@RestController
@RequestMapping("/api")
public class ProductBundleResource {

    private final Logger log = LoggerFactory.getLogger(ProductBundleResource.class);

    private static final String ENTITY_NAME = "productProductBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductBundleService productBundleService;

    public ProductBundleResource(ProductBundleService productBundleService) {
        this.productBundleService = productBundleService;
    }

    /**
     * {@code POST  /product-bundles} : Create a new productBundle.
     *
     * @param productBundle the productBundle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productBundle, or with status {@code 400 (Bad Request)} if the productBundle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-bundles")
    public ResponseEntity<ProductBundle> createProductBundle(@Valid @RequestBody ProductBundle productBundle) throws URISyntaxException {
        log.debug("REST request to save ProductBundle : {}", productBundle);
        if (productBundle.getId() != null) {
            throw new BadRequestAlertException("A new productBundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductBundle result = productBundleService.save(productBundle);
        return ResponseEntity.created(new URI("/api/product-bundles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-bundles} : Updates an existing productBundle.
     *
     * @param productBundle the productBundle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBundle,
     * or with status {@code 400 (Bad Request)} if the productBundle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productBundle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-bundles")
    public ResponseEntity<ProductBundle> updateProductBundle(@Valid @RequestBody ProductBundle productBundle) throws URISyntaxException {
        log.debug("REST request to update ProductBundle : {}", productBundle);
        if (productBundle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductBundle result = productBundleService.save(productBundle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBundle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-bundles} : get all the productBundles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productBundles in body.
     */
    @GetMapping("/product-bundles")
    public ResponseEntity<List<ProductBundle>> getAllProductBundles(Pageable pageable) {
        log.debug("REST request to get a page of ProductBundles");
        Page<ProductBundle> page = productBundleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-bundles/:id} : get the "id" productBundle.
     *
     * @param id the id of the productBundle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productBundle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-bundles/{id}")
    public ResponseEntity<ProductBundle> getProductBundle(@PathVariable Long id) {
        log.debug("REST request to get ProductBundle : {}", id);
        Optional<ProductBundle> productBundle = productBundleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productBundle);
    }

    /**
     * {@code GET  /products/{productId}/product-bundles} : Query all product bundles contains given product.
     *
     * @param productId the id of the product
     * @return the list of productBundles in body with status {@code 200 (OK)}
     */
    @GetMapping("/products/{productId}/product-bundles")
    public List<ProductBundle> getProductBundleByProduct(@PathVariable Long productId) {
        log.debug("REST request to get ProductBundle by given product : {}", productId);
        return  productBundleService.findProductBundlesByProduct(productId);
    }

    /**
     * {@code DELETE  /product-bundles/:id} : delete the "id" productBundle.
     *
     * @param id the id of the productBundle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-bundles/{id}")
    public ResponseEntity<Void> deleteProductBundle(@PathVariable Long id) {
        log.debug("REST request to delete ProductBundle : {}", id);
        productBundleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
