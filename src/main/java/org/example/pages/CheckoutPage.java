package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

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










}
