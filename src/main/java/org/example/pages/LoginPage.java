package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginPage {
    private Page page;
    private Locator emailField;
    private Locator passwordField;
    private Locator loginButton;
    private Locator error;

    public LoginPage(Page page)
    {
        this.page = page;
        this.emailField = page.locator("#email");
        this.passwordField = page.locator("#pass");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.error = page.locator(".error-msg");
    }

    public void navigate()
    {
        page.navigate("http://qa3magento.dev.evozon.com/customer/account/login/");
    }

    public void login(String email, String password)
    {
        emailField.fill(email);
        passwordField.fill(password);
        loginButton.click();
    }

    public String getErrorMessage()
    {
        return error.textContent();
    }

    public List<String> getEmailClasses()
    {
        String classes = emailField.getAttribute("class");
        if (classes == null || classes.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.asList(classes.trim().split("\\s+"));
    }
}

