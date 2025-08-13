
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import java.util.HashMap;
import java.util.Map;

public class CheckoutTest extends BaseTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/checkoutdata.csv", numLinesToSkip = 1)
    public void testCheckoutWithValidData(String firstName, String middleName, String lastName, String company, String email,
                                          String address, String streetAddress2, String city, String stateProvince,
                                          String zipCode, String country, String telephone, String fax, String password, String confirmPassword) {

        // Add product to cart
        productPage.navigateToProductPage("http://qa3magento.dev.evozon.com/camera-travel-set.html");
        productPage.selectAvailableMandatoryAttributes();
        productPage.setQuantity("1");
        productPage.clickAddToCartButton();


        Map<String, String> csvData = new HashMap<>();
        csvData.put("First Name", firstName);
        csvData.put("Middle Name", middleName);
        csvData.put("Last Name", lastName);
        csvData.put("Company", company);
        csvData.put("Email", email);
        csvData.put("Address", address);
        csvData.put("Street Address 2", streetAddress2);
        csvData.put("City", city);
        csvData.put("State/Province", stateProvince);
        csvData.put("Zip/Postal Code", zipCode);
        csvData.put("Country", country);
        csvData.put("Telephone", telephone);
        csvData.put("Fax", fax);
        csvData.put("Password", password);
        csvData.put("Confirm Password", confirmPassword);

        checkoutPage.completeCheckoutWithCsvData(csvData);

        assert checkoutPage.getNumberOfFilledFields() == checkoutPage.getNumberOfFields();
    }
}