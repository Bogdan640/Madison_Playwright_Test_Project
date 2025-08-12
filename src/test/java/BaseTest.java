import com.microsoft.playwright.*;
import org.example.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected LoginPage loginPage;
    protected ProductPage productPage;
    protected HeaderPage headerPage;
    protected RegisterPage registerPage;
    protected ComparePage comparePage;
    protected ProductListPage productListPage;
    protected ShoppingCartPage shoppingCartPage;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        page = browser.newPage();

        loginPage = new LoginPage(page);
        productPage = new ProductPage(page);
        headerPage = new HeaderPage(page);
        registerPage = new RegisterPage(page);
        comparePage = new ComparePage(page);
        productListPage = new ProductListPage(page);
        shoppingCartPage = new ShoppingCartPage(page);
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
