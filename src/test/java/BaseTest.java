import com.microsoft.playwright.*;
import org.example.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
