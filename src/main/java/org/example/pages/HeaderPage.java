package org.example.pages;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Locator;

import java.util.ArrayList;
import java.util.List;

public class HeaderPage {
    private Page page;
    private Locator logoLink;
    private Locator accountButton;
    private Locator accountDetails;
    private Locator cartButton;
    private Locator cartDetails;
    private Locator searchField;
    private Locator searchButton;
    private Locator navHeadline;
    private Locator navHeadlineDropdown;

    public HeaderPage(Page page) {
        this.page = page;
        this.logoLink = page.locator("div.page-header-container > a.logo");
        this.accountButton = page.locator("div.account-cart-wrapper > a.skip-account");
        this.accountDetails = page.locator("div#header-account");
        this.cartButton = page.locator("div.account-cart-wrapper > div.header-minicart > a.skip-cart");
        this.cartDetails = page.locator("div#header-cart");
        this.searchField = page.locator("input#search");
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        this.navHeadline = page.locator("#nav > ol.nav-primary > li.level0 > a");
        this.navHeadlineDropdown = page.locator("#nav > ol.nav-primary > li.level0 > ul.level0");
    } 

    public void navigate() {
        page.navigate("http://qa3magento.dev.evozon.com/");
    }

    public void clickLogo() {
        this.logoLink.click();
    }

    public void clickAccountButton() {
        this.accountButton.click();
    }

    public boolean isAccountDetailsVisible() {
        return this.accountDetails.isVisible();
    }

    public void clickCartButton() {
        this.cartButton.click();
    }

    public boolean isCartDetailsVisible() {
        return this.cartDetails.isVisible();
    }

    public void search(String searchInput) {
        this.searchField.clear();
        this.searchField.fill(searchInput);
        this.searchButton.click();
    }

    public void clickNavigationHeadline(String name) {
        Locator nav = this.navHeadline.and(this.page.getByText(name, new Page.GetByTextOptions().setExact(true)));
        nav.click();
    }

    public void hoverNavigationHeadline(String name) {
        Locator nav = this.navHeadline.and(this.page.getByText(name, new Page.GetByTextOptions().setExact(true)));
        nav.hover();
    }

    public List<String> getNavigationNames() {
        List<String> navHeadlines = new ArrayList<>();
        for (Locator nav : this.navHeadline.all()) {
            navHeadlines.add(nav.textContent());
        }
        return navHeadlines;
    }

    // Navigation tabs which display a dropdown when hovered over
    public List<String> getHoverableNavigationNames() {
        List<String> navHeadlines = new ArrayList<>();
        for (Locator nav : this.navHeadline.all()) {
            if (nav.innerHTML().contains("has-children"))
                navHeadlines.add(nav.textContent());
        }
        return navHeadlines;
    }

    public String getVisibleNavDropdownContent() {
        Locator navDropdown = page.getByRole(AriaRole.LIST, new Page.GetByRoleOptions().setDisabled(false)).and(this.navHeadlineDropdown);
        return navDropdown.textContent();
    }
}
