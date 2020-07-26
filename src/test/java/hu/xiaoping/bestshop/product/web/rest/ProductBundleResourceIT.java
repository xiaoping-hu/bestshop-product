package hu.xiaoping.bestshop.product.web.rest;

import hu.xiaoping.bestshop.product.ProductApp;
import hu.xiaoping.bestshop.product.config.SecurityBeanOverrideConfiguration;
import hu.xiaoping.bestshop.product.domain.ProductBundle;
import hu.xiaoping.bestshop.product.repository.ProductBundleRepository;
import hu.xiaoping.bestshop.product.service.ProductBundleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductBundleResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ProductApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class ProductBundleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ProductBundleRepository productBundleRepository;

    @Autowired
    private ProductBundleService productBundleService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductBundleMockMvc;

    private ProductBundle productBundle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBundle createEntity(EntityManager em) {
        ProductBundle productBundle = new ProductBundle()
            .name(DEFAULT_NAME);
        return productBundle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBundle createUpdatedEntity(EntityManager em) {
        ProductBundle productBundle = new ProductBundle()
            .name(UPDATED_NAME);
        return productBundle;
    }

    @BeforeEach
    public void initTest() {
        productBundle = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductBundle() throws Exception {
        int databaseSizeBeforeCreate = productBundleRepository.findAll().size();
        // Create the ProductBundle
        restProductBundleMockMvc.perform(post("/api/product-bundles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundle)))
            .andExpect(status().isCreated());

        // Validate the ProductBundle in the database
        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeCreate + 1);
        ProductBundle testProductBundle = productBundleList.get(productBundleList.size() - 1);
        assertThat(testProductBundle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createProductBundleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productBundleRepository.findAll().size();

        // Create the ProductBundle with an existing ID
        productBundle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductBundleMockMvc.perform(post("/api/product-bundles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundle)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBundle in the database
        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productBundleRepository.findAll().size();
        // set the field null
        productBundle.setName(null);

        // Create the ProductBundle, which fails.


        restProductBundleMockMvc.perform(post("/api/product-bundles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundle)))
            .andExpect(status().isBadRequest());

        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductBundles() throws Exception {
        // Initialize the database
        productBundleRepository.saveAndFlush(productBundle);

        // Get all the productBundleList
        restProductBundleMockMvc.perform(get("/api/product-bundles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBundle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getProductBundle() throws Exception {
        // Initialize the database
        productBundleRepository.saveAndFlush(productBundle);

        // Get the productBundle
        restProductBundleMockMvc.perform(get("/api/product-bundles/{id}", productBundle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productBundle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingProductBundle() throws Exception {
        // Get the productBundle
        restProductBundleMockMvc.perform(get("/api/product-bundles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductBundle() throws Exception {
        // Initialize the database
        productBundleService.save(productBundle);

        int databaseSizeBeforeUpdate = productBundleRepository.findAll().size();

        // Update the productBundle
        ProductBundle updatedProductBundle = productBundleRepository.findById(productBundle.getId()).get();
        // Disconnect from session so that the updates on updatedProductBundle are not directly saved in db
        em.detach(updatedProductBundle);
        updatedProductBundle
            .name(UPDATED_NAME);

        restProductBundleMockMvc.perform(put("/api/product-bundles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductBundle)))
            .andExpect(status().isOk());

        // Validate the ProductBundle in the database
        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeUpdate);
        ProductBundle testProductBundle = productBundleList.get(productBundleList.size() - 1);
        assertThat(testProductBundle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProductBundle() throws Exception {
        int databaseSizeBeforeUpdate = productBundleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBundleMockMvc.perform(put("/api/product-bundles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundle)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBundle in the database
        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductBundle() throws Exception {
        // Initialize the database
        productBundleService.save(productBundle);

        int databaseSizeBeforeDelete = productBundleRepository.findAll().size();

        // Delete the productBundle
        restProductBundleMockMvc.perform(delete("/api/product-bundles/{id}", productBundle.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductBundle> productBundleList = productBundleRepository.findAll();
        assertThat(productBundleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
