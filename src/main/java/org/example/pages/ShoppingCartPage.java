package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Locator;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartPage {
    private Page page;

    private Locator lastCartItem;

    private Locator emptyCartButton;
    private Locator updateShoppingCartButton;
    private Locator continueShoppingButton;
    private Locator topProceedToCheckoutButton;
    private Locator bottomProceedToCheckoutButton;
    private Locator cartSubtotal;
    private Locator cartTax;
    private Locator cartShippingTax;
    private Locator cartGrandTotal;

    public ShoppingCartPage(Page page) {
        this.page = page;
        this.lastCartItem = page.locator("table#shopping-cart-table > tbody > tr.last");
        this.emptyCartButton = page.locator("button#empty_cart_button");
        this.updateShoppingCartButton = page.locator("tfoot button.btn-update")
            .and(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setIncludeHidden(false)));
        this.continueShoppingButton = page.locator("tfoot button.btn-continue");
        this.topProceedToCheckoutButton = page.locator("ul.checkout-types.top button.btn-checkout");
        this.bottomProceedToCheckoutButton = page.locator("ul.checkout-types.bottom button.btn-checkout");

        this.cartSubtotal = page.locator("table#shopping-cart-totals-table tbody tr:has-text(\"Subtotal\"")
            .locator("span.price");

        this.cartShippingTax = page.locator("table#shopping-cart-totals-table tbody tr:has-text(\"Shipping & Handling\")")
            .locator("span.price");

        this.cartTax = page.locator("table#shopping-cart-totals-table tbody tr:has-text(\"Tax\")")
            .locator("span.price");
        
        this.cartGrandTotal = page.locator("table#shopping-cart-totals-table tfoot tr:has-text(\"Grand Total\")")
            .locator("span.price");
    }

    public void navigate() {
        this.page.navigate("http://qa3magento.dev.evozon.com/checkout/cart/");
    }

    // Page UI elements
    public void clickEmptyCartButton() {
        this.emptyCartButton.click();
    }

    public void clickUpdateShoppingCartButton() {
        this.updateShoppingCartButton.click();
    }

    public void clickContinueShoppingButton() {
        this.continueShoppingButton.click();
    }

    public void clickTopProceedToCheckoutBottom() {
        this.topProceedToCheckoutButton.click();
    }

    public void clickBottomProceedToCheckoutBottom() {
        this.bottomProceedToCheckoutButton.click();
    }

    public String getCartSubtotal() {
        return this.cartSubtotal.textContent();
    }

    public String getCartShippingTax() {
        if (this.cartShippingTax.count() == 1)
            return this.cartSubtotal.textContent();
        else
            return "";
    }

    public String getCartTax() {
        return this.cartTax.textContent();
    }

    public String getCartGrandTotal() {
        return this.cartGrandTotal.textContent();
    }


    // Last cart item specific UI elements
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

    public Map<String, String> getItemAttributes() {
        Map<String, String> attributes = new HashMap<>();
        // Check if the item has configurable attributes
        if (this.lastCartItem.locator("dl.item-options").count() == 1) {
            Locator keys = this.lastCartItem.locator("dl.item-options dt");
            Locator values = this.lastCartItem.locator("dl.item-options dd");

            for (int i = 0; i <= keys.count() - 1; i++) {
                Locator currentKey = keys.locator("nth=" + i);
                Locator currentValue = values.locator("nth=" + i);
                attributes.put(currentKey.innerText(), currentValue.innerText());
            }
        }

        return attributes;
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
