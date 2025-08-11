package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RegisterPage {
    private Page page;
    Locator firstNameField;
    Locator middleNameField;
    Locator lastNameField;
    Locator emailField;
    Locator passwordField;
    Locator confirmPasswordField;
    Locator registerButton;
    Locator succesMessage;
    Locator errorMessage;
    Locator validationAdvice;

    public RegisterPage(Page page)
    {
        this.page = page;
        this.firstNameField = page.locator("#firstname");
        this.middleNameField = page.locator("#middlename");
        this.lastNameField = page.locator("#lastname");
        this.emailField = page.locator("#email_address");
        this.passwordField = page.locator("#password");
        this.confirmPasswordField = page.locator("#confirmation");
        this.registerButton = page.locator("button[title=\"Register\"]");
        this.succesMessage = page.locator(".success-msg");
        this.errorMessage = page.locator(".error-msg");
        this.validationAdvice = page.locator(".validation-advice");
    }

    public void navigate()
    {
        page.navigate("http://qa3magento.dev.evozon.com/customer/account/create/");
    }

    public void clickRegisterButton()
    {
        registerButton.click();
    }

    public void register(String firstName, String middleName, String lastName, String email, String password, String confirmPassword)
    {
        firstNameField.fill(firstName);
        middleNameField.fill(middleName);
        lastNameField.fill(lastName);
        emailField.fill(email);
        passwordField.fill(password);
        confirmPasswordField.fill(confirmPassword);
    }

    public String getSuccessMessage()
    {
        return succesMessage.textContent();
    }

    public String getErrorMessage()
    {
        return errorMessage.textContent();
    }

    public String getValidationAdvice()
    {
        return validationAdvice.first().textContent();
    }

}
