import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class AddToCartTest extends BaseTest {
    @Test
    public void testProductPageToCartFlow() {
        this.productPage.navigateToProductPage("http://qa3magento.dev.evozon.com/camera-travel-set.html");
        productPage.selectAvailableMandatoryAttributes();
        productPage.selectAvailableOptionalAttributes();
        productPage.setQuantity("2");

        String productName = this.productPage.getProductName();
        String productPrice = this.productPage.getPrice();
        int productQuantity = this.productPage.getProductQuantity();
        Map<String, String> productAttributes = this.productPage.getSelectedAttributes(); 

        productPage.clickAddToCartButton();

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/checkout/cart/");
        assertEquals(this.shoppingCartPage.getItemName(), productName);
        assertEquals(this.shoppingCartPage.getItemPrice(), productPrice);
        assertEquals(this.shoppingCartPage.getItemQuantity(), productQuantity);

        for (java.util.Map.Entry<String, String> item : this.shoppingCartPage.getItemAttributes().entrySet()) {
            System.out.println("Key: " + item.getKey() + ", value: " + item.getValue());
        }

        for (java.util.Map.Entry<String, String> item : productAttributes.entrySet()) {
            System.out.println("Key: " + item.getKey() + ", value: " + item.getValue());
        }

        for (java.util.Map.Entry<String, String> item : this.shoppingCartPage.getItemAttributes().entrySet()) {
            String currentKey = item.getKey();
            assertNotNull(productAttributes.get(currentKey));
            String ceva = productAttributes.get(currentKey);
            assertTrue(ceva.contains(item.getValue()));
        }
    }   
}
