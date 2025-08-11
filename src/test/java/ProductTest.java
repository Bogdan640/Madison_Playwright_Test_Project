import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductTest extends BaseTest {
    private static final String SampleProductURL = "http://qa3magento.dev.evozon.com/elizabeth-knit-top-601.html";



    @Test
    public void testNavigateToProductPage() {

        productPage.navigateToProductPage(SampleProductURL);
        assertThat(page).hasURL(SampleProductURL);

    }



    @Test
    public void testSelectValidMandatoryAttributes() {

        productPage.navigateToProductPage(SampleProductURL);
        if (page.locator("#product-options-wrapper ul[id *='swatch']").count() > 0 ||
                page.locator("#product-options-wrapper select.required-entry[id*='bundle']").count() > 0) {

            productPage.selectAvailableMandatoryAttributes();
            assertThat(page.locator("#product-options-wrapper")).isVisible();
        }
    }

    @Test
    public void testSelectValidOptionalAttributes() {
        productPage.navigateToProductPage(SampleProductURL);

        if (page.locator("#product-options-wrapper select:not(.required-entry)~ul[id *='swatch']").count() > 0 ||
                page.locator("#product-options-wrapper dd select:not(.required-entry)").count() > 0) {

            productPage.selectAvailableOptionalAttributes();
            assertThat(page.locator("#product-options-wrapper")).isVisible();
        }
    }

    @Test
    public void testClickAddToCartWithoutAttributes() {

        productPage.navigateToProductPage(SampleProductURL);
        productPage.clickAddToCartButton();
        assertThat(page).hasURL(SampleProductURL);
    }

    @Test
    public void testAddToCartWithMandatoryAttributesAndQuantity() {

        productPage.navigateToProductPage(SampleProductURL);
        productPage.selectAvailableMandatoryAttributes();
        productPage.setQuantity("2");
        productPage.clickAddToCartButton();
        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/checkout/cart/");
    }




    @Test
    public void testInvalidQuantityHandling() {

        productPage.navigateToProductPage(SampleProductURL);
        productPage.setQuantity("0");
        productPage.selectAvailableMandatoryAttributes();
        productPage.clickAddToCartButton();
        assertThat(page).hasURL(SampleProductURL);
    }

    @Test
    public void testNegativeQuantityHandling() {

        productPage.navigateToProductPage(SampleProductURL);
        productPage.setQuantity("-1");
        productPage.selectAvailableMandatoryAttributes();
        productPage.clickAddToCartButton();
        assertThat(page).hasURL(SampleProductURL);
    }










}