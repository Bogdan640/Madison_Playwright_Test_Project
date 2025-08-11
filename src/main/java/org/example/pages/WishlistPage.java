package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class WishlistPage {
    private Page page;
    private String title;
    private Locator successMessage;
    private Locator productsList;
    private Locator image;
    private Locator productTitle;
    private Locator productSKU;
    private Locator productQuantity;
    private Locator productPrice;
    private Locator commentsField;
    private Locator updateWishlistProductButton;
    private Locator addToCartButton;
    private Locator editButton;
    private Locator viewDetailsButton;
    private Locator removeButton;
    private Locator updateWishlistButton;
    private Locator addAllToCartButton;
    private Locator shareWishlistButton;
    public WishlistPage(Page page)
    {
        this.page = page;
        this.title = page.title();
        this.successMessage = page.locator(".success-msg");
        this.productsList = page.locator("#wishlist-table tbody tr");
        this.image = page.locator("#wishlist-table tbody tr:last-child img");
        this.productTitle = page.locator("#wishlist-table tbody tr:last-child .product-name>a");
        this.productSKU = page.locator("#wishlist-table tbody tr:last-child .wishlist-sku>span");
        this.productQuantity = page.locator("#wishlist-table tbody tr:last-child .input-text.qty");
        this.productPrice = page.locator("#wishlist-table tbody tr:last-child .price");
        this.commentsField = page.locator("#wishlist-table tbody tr:last-child textarea");
        this.updateWishlistProductButton = page.locator("#wishlist-table tbody tr:last-child .btn-update");
        this.addToCartButton = page.locator("#wishlist-table tbody tr:last-child .btn-cart");
        this.editButton = page.locator("#wishlist-table tbody tr:last-child .link-edit");
        this.viewDetailsButton = page.locator("#wishlist-table tbody tr:last-child .details");
        this.removeButton = page.locator("#wishlist-table tbody tr:last-child .btn-remove");
        this.updateWishlistButton = page.locator(".buttons-set.buttons-set2 .btn-update");
        this.addAllToCartButton = page.locator(".buttons-set.buttons-set2 .btn-add");
        this.shareWishlistButton = page.locator(".buttons-set.buttons-set2 .btn-share");
    }

    public String getSuccessMessage()
    {
        return successMessage.textContent();
    }

    public int getNumberOfProducts()
    {
        return productsList.count();
    }

    public String getImageSource()
    {
        return image.getAttribute("src");
    }

    public String getProductTitle()
    {
        return productTitle.textContent();
    }

    public String getProductSKU()
    {
        return productSKU.textContent();
    }

    public int getProductQuantity()
    {
        return Integer.parseInt(productQuantity.inputValue());
    }
    public void setProductQuantity(int quantity)
    {
        productQuantity.fill(String.valueOf(quantity));
    }

    public float getProductPrice()
    {
        String price = productPrice.textContent();
        float value = Float.parseFloat(price.replace("$", ""));
        return value;
    }

    public void fillCommentsSection(String comment)
    {
        commentsField.fill(comment);
    }

    public void clickUpdateWishlistProductButton()
    {
        updateWishlistProductButton.click();
    }

    public void clickAddToCartButton()
    {
        addToCartButton.click();
    }

    public void clickEditButton()
    {
        editButton.click();
    }

    public void clickViewDetailsButton()
    {
        viewDetailsButton.click();
    }
    public void clickRemoveButton()
    {
        removeButton.click();
    }

    public void clickUpdateWishlistButton()
    {
        updateWishlistButton.click();
    }

    public void clickAddAllToCartButton()
    {
        addAllToCartButton.click();
    }
    public void clickShareWishlistButton()
    {
        shareWishlistButton.click();
    }
}
