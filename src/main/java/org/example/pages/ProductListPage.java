package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProductListPage{
    private Page page;
    private final Locator addToCompareButton;
    private final String productName = "samponel";
    private final Locator viewProductPageByProductName;


    public ProductListPage(Page page) {
        this.page = page;
        addToCompareButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Compare"));
        viewProductPageByProductName = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Samponel")).getByRole(AriaRole.LINK);
    }



    public void clickCompareButton() {
        addToCompareButton.click();
    }

    public void clickViewProductPageByProductName() {
        viewProductPageByProductName.click();
    }

}
