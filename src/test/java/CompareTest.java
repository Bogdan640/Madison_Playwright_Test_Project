
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CompareTest extends BaseTest{

    private static final String PRODUCT_URL = "http://qa3magento.dev.evozon.com/samponel.html";

    @Test
    public void testAddProductToCompare() {
        productPage.navigateToProductPage(PRODUCT_URL);

        // Store product name and price for later verification
        String productName = productPage.getProductName();
        String productPrice = productPage.getPrice();
        System.out.println("Adding product to compare: " + productName + ", Price: " + productPrice);

        // Select mandatory attributes if present
         productPage.selectAvailableMandatoryAttributes();
         page.waitForTimeout(5000);

        // Add product to compare
        productPage.clickAddToCompareButton();
        page.waitForTimeout(5000);


        headerPage.search("e");
        page.waitForTimeout(5000);

        productListPage.clickCompareButton();
        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/catalog/product_compare/index/");



        // Verify product details in compare page
        assertThat(page.locator(".product-name")).containsText(productName);

        // Print all products in compare page
        System.out.println("Products in compare page:");
        for (int i = 0; i < page.locator(".product-name").count(); i++) {
            String name = page.locator(".product-name").nth(i).innerText();
            String price = page.locator(".price").nth(i).innerText();
            System.out.println("Product: " + name + ", Price: " + price);
        }
    }

}
