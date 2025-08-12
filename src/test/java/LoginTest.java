import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/validlogindata.csv", numLinesToSkip = 1)
    public void parameterizedTestLoginValidCredentials(String email, String password)
    {
        page.setDefaultTimeout(100000);

        loginPage.navigate();
        loginPage.login(email, password);

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/customer/account/");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidlogindata.csv", numLinesToSkip = 1)
    public void testLoginInvalidCredentials(String email, String password)
    {
        loginPage.navigate();
        loginPage.login(email, password);

        assert loginPage.getErrorMessage().equals("Invalid login or password.");
    }

    @Test
    public void testLoginEmptyEmail()
    {
        loginPage.navigate();
        loginPage.login("", "abcdef123");

        assert loginPage.getValidationAdvice().equals("This is a required field.");
    }

    @Test
    public void testLoginEmptyPassword()
    {
        loginPage.navigate();
        loginPage.login("email.com", "");

        Locator locator = page.getByRole(AriaRole.ALERT);
        System.out.println(locator.textContent());

        assert loginPage.getErrorMessage().equals("Login and password are required.");
    }



}

