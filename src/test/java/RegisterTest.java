import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.example.pages.RegisterPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest{
    @Test
    public void testRegisterValidInformation()
    {
        registerPage.navigate();
        registerPage.register("name", "name", "name", "email102@email.com", "abcdef123", "abcdef123");
        registerPage.clickRegisterButton();

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/customer/account/index/");
        assert registerPage.getSuccessMessage().equals("Thank you for registering with Madison Island.");
    }

    @Test
    public void testRegisterExistingAccount()
    {
        registerPage.navigate();
        registerPage.register("name", "name", "name", "email102@email.com", "abcdef123", "abcdef123");
        registerPage.clickRegisterButton();

        assert registerPage.getErrorMessage().contains("There is already an account with this email address");
    }

    @Test
    public void testRegisterEmptyFields()
    {
        registerPage.navigate();
        registerPage.register("", "", "", "", "", "");
        registerPage.clickRegisterButton();

        assert registerPage.getValidationAdvice().equals("This is a required field.");
    }
}
