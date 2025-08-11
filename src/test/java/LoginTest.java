import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {



    @Test
    public void testLoginValidCredentials()
    {
        page.setDefaultTimeout(100000);

        loginPage.navigate();
        loginPage.login("email@email.com", "abcdef123");

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/customer/account/");
    }

    @Test
    public void testLoginInvalidCredentials()
    {
        loginPage.navigate();
        loginPage.login("email@email.com", "abc123");

        assert loginPage.getErrorMessage().equals("Invalid login or password.");
    }

    @Test
    public void testLoginEmptyEmail()
    {
        loginPage.navigate();
        loginPage.login("", "abcdef123");

        assert loginPage.getEmailClasses().contains("validation-failed");
    }

    @Test
    public void testLoginEmptyPassword()
    {
        loginPage.navigate();
        loginPage.login("email@email.com", "");

        assert loginPage.getErrorMessage().equals("Login and password are required.");
    }


}

