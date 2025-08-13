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

    @ParameterizedTest
    @CsvFileSource(resources = "/validregisterdata.csv", numLinesToSkip = 1)
    public void testRegisterValidInformation(String firstName, String middleName, String lastName, String email, String password, String confirmPassword)
    {
        registerPage.navigate();
        registerPage.register(firstName, middleName, lastName, email, password, confirmPassword);
        registerPage.clickRegisterButton();

        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/customer/account/index/");
        assert registerPage.getSuccessMessage().equals("Thank you for registering with Madison Island.");

        generateUsers("src/main/resources/validregisterdata.csv");
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
