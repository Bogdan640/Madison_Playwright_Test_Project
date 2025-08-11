
import com.microsoft.playwright.*;
import org.example.pages.HeaderPage;
import org.example.pages.LoginPage;
import org.example.pages.ProductPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected LoginPage loginPage;
    protected ProductPage productPage;
    protected HeaderPage headerPage;


    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        //browser = playwright.firefox().launch();

        page = browser.newPage();

        loginPage = new LoginPage(page);
        productPage = new ProductPage(page);
        headerPage = new HeaderPage(page);



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
