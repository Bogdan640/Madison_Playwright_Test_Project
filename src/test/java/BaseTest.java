import com.microsoft.playwright.*;
import org.example.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public abstract class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected BrowserContext context;
    protected LoginPage loginPage;
    protected ProductPage productPage;
    protected HeaderPage headerPage;
    protected RegisterPage registerPage;
    protected ComparePage comparePage;
    protected ProductListPage productListPage;
    protected CheckoutPage checkoutPage;


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
        checkoutPage = new CheckoutPage(page);


        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));
    }

    @AfterEach
    void tearDown() {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            io.qameta.allure.Allure.addAttachment(
                    "Screenshot",
                    "image/png",
                    new java.io.ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
        if (context != null) {
            try {
                Path tracePath = Paths.get("target/traces", getClass().getSimpleName() + "-" +
                        System.currentTimeMillis() + ".zip");
                Files.createDirectories(tracePath.getParent());
                context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
            } catch (Exception e) {
                System.err.println("Failed to save trace: " + e.getMessage());
            }

            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
