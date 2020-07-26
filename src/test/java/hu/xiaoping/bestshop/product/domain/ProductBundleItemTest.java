package hu.xiaoping.bestshop.product.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hu.xiaoping.bestshop.product.web.rest.TestUtil;

public class ProductBundleItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBundleItem.class);
        ProductBundleItem productBundleItem1 = new ProductBundleItem();
        productBundleItem1.setId(1L);
        ProductBundleItem productBundleItem2 = new ProductBundleItem();
        productBundleItem2.setId(productBundleItem1.getId());
        assertThat(productBundleItem1).isEqualTo(productBundleItem2);
        productBundleItem2.setId(2L);
        assertThat(productBundleItem1).isNotEqualTo(productBundleItem2);
        productBundleItem1.setId(null);
        assertThat(productBundleItem1).isNotEqualTo(productBundleItem2);
    }
}
