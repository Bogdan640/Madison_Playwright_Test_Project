package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Locator;

public class ShoppingCartPage {
    private Page page;

    private Locator lastCartItem;
    private Locator emptyCartButton;
    private Locator updateShoppingCartButton;
    private Locator continueShoppingButton;

    public ShoppingCartPage(Page page) {
        this.page = page;
        this.lastCartItem = page.locator("table#shopping-cart-table > tbody > tr.last");
        this.emptyCartButton = page.locator("button#empty_cart_button");
        this.updateShoppingCartButton = page.locator("tfoot button.btn-update")
            .and(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setIncludeHidden(false)));
        this.continueShoppingButton = page.locator("tfoot button.btn-continue");
    }

    public void navigate() {
        this.page.navigate("http://qa3magento.dev.evozon.com/checkout/cart/");
    }

    public void clickEmptyCartButton() {
        this.emptyCartButton.click();
    }

    public void clickUpdateShoppingCartButton() {
        this.updateShoppingCartButton.click();
    }

    public void clickContinueShoppingButton() {
        this.continueShoppingButton.click();
    }

    public String getItemName() {
        Locator itemName = this.lastCartItem.locator("h2.product-name > a");
        return itemName.textContent();
    }

    public String getItemSKU() {
        Locator itemSKU = this.lastCartItem.locator("div.product-cart-sku > span");
        return itemSKU.textContent();
    }

    public String getItemPrice() {
        Locator itemPrice = this.lastCartItem.locator("td.product-cart-price > span.cart-price > span");
        return itemPrice.textContent();
    }

    public String getItemSubtotal() {
        Locator itemSubtotal = this.lastCartItem.locator("td.product-cart-total > span.cart-price > span");
        return itemSubtotal.textContent();
    }

    public int getItemQuantity() {
        Locator itemQtyField = this.lastCartItem.locator("input[title=\"Qty\"]");
        return Integer.parseInt(itemQtyField.inputValue());
    }

    public void focusOverItemQuantity() {
        Locator itemQtyField = this.lastCartItem.locator("input[title=\"Qty\"]");
        itemQtyField.focus();
    }

    public void fillItemQuantity(String input) {
        Locator itemQtyField = this.lastCartItem.locator("input[title=\"Qty\"]");
        itemQtyField.fill(input);
    }

    public void clickItemEditButton() {
        Locator editButton = this.lastCartItem.locator("td.product-cart-actions ul.cart-links a[title=\"Edit item parameters\"]");
        editButton.click();
    }

    public void clickMoveToWishlistButton() {
        Locator moveToWishlistButton = this.lastCartItem.locator("td.product-cart-actions ul.cart-links a.link-wishlist");
        moveToWishlistButton.click();
    }

    public void clickDeleteItemButton() {
        Locator deleteItemButton = this.lastCartItem.locator("td.product-cart-remove > a");
        deleteItemButton.click();
    }

    public void clickUpdateItemButton() {
        this.focusOverItemQuantity();
        Locator updateItemButton = this.lastCartItem.locator("td.product-cart-actions > button.btn-update");
        updateItemButton.click();
    }
}
