import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.example.pages.ProductPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPageTest {
    private static Playwright playwright;
    private static Browser browser;
    private Page page;
    private ProductPage productPage;
    private static final String SampleProductURL = "http://qa3magento.dev.evozon.com/elizabeth-knit-top-601.html";

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        productPage = new ProductPage(page);
    }

    @Test
    public void testNavigateToProductPage() {

        productPage.navigateToProductPage(SampleProductURL);
        assertThat(page).hasURL(SampleProductURL);

    }



    @Test
    public void testSelectMandatoryAttributes() {

        productPage.navigateToProductPage(SampleProductURL);
        if (page.locator("#product-options-wrapper ul[id *='swatch']").count() > 0 ||
                page.locator("#product-options-wrapper select.required-entry[id*='bundle']").count() > 0) {

            productPage.selectAvailableMandatoryAttributes();
            assertThat(page.locator("#product-options-wrapper")).isVisible();
        }
    }

    @Test
    public void testSelectOptionalAttributes() {
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



    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}