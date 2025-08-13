import com.microsoft.playwright.*;
import org.example.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = new Random();

    private static String randomString(int minLen, int maxLen) {
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return sb.toString();
    }

    private static String generateEmail(String firstName, String lastName) {
        String domain = "example.com";
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domain;
    }

    private static void generateUsers(String filename)
    {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("First Name,Middle Name,Last Name,Email,Password,Confirm Password\n");

            for (int i = 0; i < 10; i++) {
                String firstName = randomString(5, 10);
                String middleName = randomString(3, 7);
                String lastName = randomString(5, 12);

                String email = generateEmail(firstName, lastName);
                String password = randomString(8, 12);
                String confirmPassword = password;

                writer.append(String.format("%s,%s,%s,%s,%s,%s\n",
                        firstName, middleName, lastName, email, password, confirmPassword));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void setUpAll()
    {
        generateUsers("src/main/resources/validregisterdata.csv");
    }


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
