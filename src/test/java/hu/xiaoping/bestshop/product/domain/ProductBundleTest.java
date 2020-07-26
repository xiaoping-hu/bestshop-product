package hu.xiaoping.bestshop.product.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hu.xiaoping.bestshop.product.web.rest.TestUtil;

public class ProductBundleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBundle.class);
        ProductBundle productBundle1 = new ProductBundle();
        productBundle1.setId(1L);
        ProductBundle productBundle2 = new ProductBundle();
        productBundle2.setId(productBundle1.getId());
        assertThat(productBundle1).isEqualTo(productBundle2);
        productBundle2.setId(2L);
        assertThat(productBundle1).isNotEqualTo(productBundle2);
        productBundle1.setId(null);
        assertThat(productBundle1).isNotEqualTo(productBundle2);
    }
}
