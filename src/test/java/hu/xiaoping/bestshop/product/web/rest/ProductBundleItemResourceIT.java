package hu.xiaoping.bestshop.product.web.rest;

import hu.xiaoping.bestshop.product.ProductApp;
import hu.xiaoping.bestshop.product.config.SecurityBeanOverrideConfiguration;
import hu.xiaoping.bestshop.product.domain.ProductBundleItem;
import hu.xiaoping.bestshop.product.repository.ProductBundleItemRepository;
import hu.xiaoping.bestshop.product.service.ProductBundleItemService;

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
 * Integration tests for the {@link ProductBundleItemResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ProductApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class ProductBundleItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_DISCOUNT_AMOUNT = 1;
    private static final Integer UPDATED_DISCOUNT_AMOUNT = 2;

    @Autowired
    private ProductBundleItemRepository productBundleItemRepository;

    @Autowired
    private ProductBundleItemService productBundleItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductBundleItemMockMvc;

    private ProductBundleItem productBundleItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBundleItem createEntity(EntityManager em) {
        ProductBundleItem productBundleItem = new ProductBundleItem()
            .quantity(DEFAULT_QUANTITY)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT);
        return productBundleItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBundleItem createUpdatedEntity(EntityManager em) {
        ProductBundleItem productBundleItem = new ProductBundleItem()
            .quantity(UPDATED_QUANTITY)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT);
        return productBundleItem;
    }

    @BeforeEach
    public void initTest() {
        productBundleItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductBundleItem() throws Exception {
        int databaseSizeBeforeCreate = productBundleItemRepository.findAll().size();
        // Create the ProductBundleItem
        restProductBundleItemMockMvc.perform(post("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundleItem)))
            .andExpect(status().isCreated());

        // Validate the ProductBundleItem in the database
        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProductBundleItem testProductBundleItem = productBundleItemList.get(productBundleItemList.size() - 1);
        assertThat(testProductBundleItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProductBundleItem.getDiscountAmount()).isEqualTo(DEFAULT_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void createProductBundleItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productBundleItemRepository.findAll().size();

        // Create the ProductBundleItem with an existing ID
        productBundleItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductBundleItemMockMvc.perform(post("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundleItem)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBundleItem in the database
        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productBundleItemRepository.findAll().size();
        // set the field null
        productBundleItem.setQuantity(null);

        // Create the ProductBundleItem, which fails.


        restProductBundleItemMockMvc.perform(post("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundleItem)))
            .andExpect(status().isBadRequest());

        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiscountAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = productBundleItemRepository.findAll().size();
        // set the field null
        productBundleItem.setDiscountAmount(null);

        // Create the ProductBundleItem, which fails.


        restProductBundleItemMockMvc.perform(post("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundleItem)))
            .andExpect(status().isBadRequest());

        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductBundleItems() throws Exception {
        // Initialize the database
        productBundleItemRepository.saveAndFlush(productBundleItem);

        // Get all the productBundleItemList
        restProductBundleItemMockMvc.perform(get("/api/product-bundle-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBundleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getProductBundleItem() throws Exception {
        // Initialize the database
        productBundleItemRepository.saveAndFlush(productBundleItem);

        // Get the productBundleItem
        restProductBundleItemMockMvc.perform(get("/api/product-bundle-items/{id}", productBundleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productBundleItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.discountAmount").value(DEFAULT_DISCOUNT_AMOUNT));
    }
    @Test
    @Transactional
    public void getNonExistingProductBundleItem() throws Exception {
        // Get the productBundleItem
        restProductBundleItemMockMvc.perform(get("/api/product-bundle-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductBundleItem() throws Exception {
        // Initialize the database
        productBundleItemService.save(productBundleItem);

        int databaseSizeBeforeUpdate = productBundleItemRepository.findAll().size();

        // Update the productBundleItem
        ProductBundleItem updatedProductBundleItem = productBundleItemRepository.findById(productBundleItem.getId()).get();
        // Disconnect from session so that the updates on updatedProductBundleItem are not directly saved in db
        em.detach(updatedProductBundleItem);
        updatedProductBundleItem
            .quantity(UPDATED_QUANTITY)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT);

        restProductBundleItemMockMvc.perform(put("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductBundleItem)))
            .andExpect(status().isOk());

        // Validate the ProductBundleItem in the database
        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeUpdate);
        ProductBundleItem testProductBundleItem = productBundleItemList.get(productBundleItemList.size() - 1);
        assertThat(testProductBundleItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProductBundleItem.getDiscountAmount()).isEqualTo(UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingProductBundleItem() throws Exception {
        int databaseSizeBeforeUpdate = productBundleItemRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBundleItemMockMvc.perform(put("/api/product-bundle-items").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productBundleItem)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBundleItem in the database
        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductBundleItem() throws Exception {
        // Initialize the database
        productBundleItemService.save(productBundleItem);

        int databaseSizeBeforeDelete = productBundleItemRepository.findAll().size();

        // Delete the productBundleItem
        restProductBundleItemMockMvc.perform(delete("/api/product-bundle-items/{id}", productBundleItem.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductBundleItem> productBundleItemList = productBundleItemRepository.findAll();
        assertThat(productBundleItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
