package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutPage {
    private Page page;
    private final String CheckoutURL = "http://qa3magento.dev.evozon.com/checkout/onepage/";

    private final Locator guestCheckoutRadioButton;
    private final Locator registerAndCheckoutRadioButton;
    private final Locator continueButton;
    private final Locator textInputFields;
    private final Locator dropdownFields;
    private final Locator shipThisAddressButton;
    private final Locator shipDifferentAddressButton;
    private final Locator requiredEntryErrorMessage;


    public CheckoutPage(Page page) {
        this.page = page;
        guestCheckoutRadioButton = page.locator("input[id='login:guest']");
        registerAndCheckoutRadioButton = page.locator("input[id='login:register']");
        continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("continue"));
        textInputFields = page.locator("[id^='billing'] li:not([style*='display: none']) .input-box input");
        dropdownFields = page.locator("[id^='billing'] .input-box select");
        shipThisAddressButton = page.getByText("Ship to this address");
        shipDifferentAddressButton = page.getByText("Ship to different address");
        requiredEntryErrorMessage = page.locator("[id^='advice-required'] ");

    }

    //TODO: some methods that allow the test functions to get the fields labeled as
    // required and the fields that are actually required


    public void navigateToCheckout() {
        page.navigate(CheckoutURL);
    }

    public int getNumberOfRequiredFieldsErrors() {
        return requiredEntryErrorMessage.count();
    }



    public void clickGuestCheckoutRadioButton(){
        assertThat(guestCheckoutRadioButton).isVisible();
        assertThat(guestCheckoutRadioButton).isEnabled();
        guestCheckoutRadioButton.click();
        assertThat(registerAndCheckoutRadioButton).not().isChecked();

    }

    public void clickRegisterAndCheckoutRadioButton(){
        assertThat(registerAndCheckoutRadioButton).isEnabled();
        assertThat(registerAndCheckoutRadioButton).isVisible();
        registerAndCheckoutRadioButton.click();
        assertThat(guestCheckoutRadioButton).not().isChecked();
    }

    public void clickContinueButton(){
        assertThat(continueButton).isEnabled();
        continueButton.click();
    }

    public void clickShipThisAddressButton(){
        assertThat(shipThisAddressButton).isEnabled();
        assertThat(shipThisAddressButton).isVisible();
        shipThisAddressButton.click();
        assertThat(shipDifferentAddressButton).not().isChecked();
    }

    public void clickShipDifferentAddressButton(){
        assertThat(shipDifferentAddressButton).isEnabled();
        assertThat(shipDifferentAddressButton).isVisible();
        shipDifferentAddressButton.click();
        assertThat(shipThisAddressButton).not().isChecked();
    }

    public int getNumberOfFields(){
        return textInputFields.count() + dropdownFields.count();
    }


    public void fillCheckoutForm(Map<String, String> csvData) {
        fillTextInputFields(csvData);
        fillDropdownFields(csvData);
    }



    private void fillTextInputFields(Map<String, String> csvData) {

        for (int i = 0; i < textInputFields.count(); i++) {

            Locator field = textInputFields.nth(i);
            String id = field.getAttribute("id");
            String name = field.getAttribute("name");

            if (id != null) {
                // Map field ID to CSV data column based on field name patterns
                if (id.contains("firstname") && csvData.containsKey("First Name")) {
                    field.fill(csvData.get("First Name"));
                } else if (id.contains("middlename") && csvData.containsKey("Middle Name")) {
                    field.fill(csvData.get("Middle Name"));
                } else if (id.contains("lastname") && csvData.containsKey("Last Name")) {
                    field.fill(csvData.get("Last Name"));
                } else if (id.contains("company") && csvData.containsKey("Company")) {
                    field.fill(csvData.get("Company"));
                } else if (id.contains("email") && csvData.containsKey("Email")) {
                    field.fill(csvData.get("Email"));
                } else if (id.contains("street1") && csvData.containsKey("Address")) {
                    field.fill(csvData.get("Address"));
                } else if (id.contains("street2") && csvData.containsKey("Street Address 2")) {
                    field.fill(csvData.get("Street Address 2"));
                } else if (id.contains("city") && csvData.containsKey("City")) {
                    field.fill(csvData.get("City"));
                } else if (id.contains("postcode") && csvData.containsKey("Zip/Postal Code")) {
                    field.fill(csvData.get("Zip/Postal Code"));
                } else if (id.contains("telephone") && csvData.containsKey("Telephone")) {
                    field.fill(csvData.get("Telephone"));
                } else if (id.contains("fax") && csvData.containsKey("Fax")) {
                    field.fill(csvData.get("Fax"));
                } else if (id.contains("password") && !id.contains("confirm") && csvData.containsKey("Password")) {
                    field.fill(csvData.get("Password"));
                } else if (id.contains("confirmation") && csvData.containsKey("Confirm Password")) {
                    field.fill(csvData.get("Confirm Password"));
                }
            }
        }
    }

    // Method to fill dropdown fields based on field labels
    private void fillDropdownFields(Map<String, String> csvData) {
        for (int i = 0; i < dropdownFields.count(); i++) {
            Locator field = dropdownFields.nth(i);
            String id = field.getAttribute("id");

            if (id != null) {
                // Handle country dropdown
                if (id.contains("country_id") && csvData.containsKey("Country")) {
                    field.selectOption(new SelectOption().setLabel(csvData.get("Country")));
                    page.waitForTimeout(500);
                }
                else if ((id.contains("region_id") || id.contains("state")) && csvData.containsKey("State/Province")) {
                    field.selectOption(new SelectOption().setLabel(csvData.get("State/Province")));
                }
            }
        }
    }

    // Complete checkout process with data from CSV
    public void completeCheckoutWithCsvData(Map<String, String> csvData) {
        navigateToCheckout();
        clickGuestCheckoutRadioButton();
        clickContinueButton();

        fillCheckoutForm(csvData);


        clickShipThisAddressButton();
        clickContinueButton();
    }

    public int getNumberOfFilledFields() {


        int count = 0;
        System.out.println("Checking filled fields...");

        for (int i = 0; i < textInputFields.count(); i++) {
            Locator field = textInputFields.nth(i);
            String value = field.inputValue();
            String id = field.getAttribute("id");

            if (value != null && !value.trim().isEmpty()) {
                count++;
            }
        }

        for (int i = 0; i < dropdownFields.count(); i++) {
            Locator field = dropdownFields.nth(i);
            String id = field.getAttribute("id");
            String selectedValue = field.evaluate("el => el.options[el.selectedIndex]?.value || ''").toString();

            if (selectedValue != null && !selectedValue.isEmpty() && !selectedValue.equals("")) {
                String selectedText = field.evaluate("el => el.options[el.selectedIndex]?.text || ''").toString();
                System.out.println("Dropdown filled: " + id + " = " + selectedText + " (value: " + selectedValue + ")");
                count++;
            }
        }

        return count;
    }













}
