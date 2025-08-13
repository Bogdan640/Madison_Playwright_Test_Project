import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.example.pages.RegisterPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest{

    @ParameterizedTest
    @CsvFileSource(resources = "/validregisterdata.csv", numLinesToSkip = 1)
    public void testRegisterValidInformation(String firstName, String middleName, String lastName, String email, String password, String confirmPassword)
    {
        registerPage.navigate();
        registerPage.register(firstName, middleName, lastName, email, password, confirmPassword);
        registerPage.clickRegisterButton();

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/customer/account/index/");
        assert registerPage.getSuccessMessage().equals("Thank you for registering with Madison Island.");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/existingaccountdata.csv", numLinesToSkip = 1)
    public void testRegisterExistingAccount(String firstName, String middleName, String lastName, String email, String password, String confirmPassword)
    {
        registerPage.navigate();
        registerPage.register(firstName, middleName, lastName, email, password, confirmPassword);
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
