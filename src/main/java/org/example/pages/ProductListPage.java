package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductListPage{
    private Page page;
    private final Locator addToCompareButton;


    public ProductListPage(Page page) {
        this.page = page;
        addToCompareButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Compare"));
    }

    public void clickCompareButton() {
        addToCompareButton.click();
    }
}
