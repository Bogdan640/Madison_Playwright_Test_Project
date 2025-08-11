package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import java.util.ArrayList;
import java.util.List;

public class ProductPage {
    private Page page;
    private final Locator quantityInput;
    private final Locator addToCartButton;
    private final Locator addToWishlistButton;
    private final Locator addToCompareButton;

    private final Locator swatchGroups;
    private final Locator optionalSwatchGroups;

    private final Locator dropdowns;
    private final Locator mandatoryDropdowns;
    private final Locator optionalDropdowns;

    //price
    private final Locator productCurrentPrice;
    private final Locator productPriceAsConfigured;
    private final Locator productStartingPrice;
    private final Locator productMaximumPrice;





    private final Locator productName;

    public ProductPage(Page page) {
        this.page = page;
        this.quantityInput = page.locator("#qty");
        //buttons
        this.addToCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
        this.addToCompareButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Compare"));
        this.addToWishlistButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Add to Wishlist"));

        //attributes

        //Swatches
        this.optionalSwatchGroups = page.locator("#product-options-wrapper select:not(.required-entry)~ul[id *='swatch']");
        this.swatchGroups = page.locator("#product-options-wrapper ul[id *='swatch']");

        //dropdowns
        this.dropdowns = page.getByText("Choose a selection...");
        this.mandatoryDropdowns = page.locator("#product-options-wrapper select.required-entry[id*='bundle']");
        this.optionalDropdowns = page.locator("#product-options-wrapper dd select:not(.required-entry)");


        //price
        this.productCurrentPrice = page.locator("[id^=product-price] .price").first();
        this.productPriceAsConfigured = page.getByText("Price as configured:");
        this.productStartingPrice = page.getByText("From:");
        this.productMaximumPrice = page.getByText("To:");



        this.productName = page.locator(".product-shop .product-name");
    }


    public boolean hasDropdowns() {
        return dropdowns.count() > 0;
    }

    public boolean hasSwatchGroups() {
        return swatchGroups.count() > 0;
    }


    //price section
    public String getStartingPrice() {
        if (productCurrentPrice.count() > 0){
            return productCurrentPrice.innerText();
        }
        else if(productStartingPrice.count() > 0){
            return productStartingPrice.innerText();
        }
        else{
            return null;
        }
    }

    public String getMaximumPrice() {
        if (productCurrentPrice.count() > 0){
            return productCurrentPrice.innerText();
        }
        else if(productMaximumPrice.count() > 0){
            return productMaximumPrice.innerText();
        }
        else{
            return null;
        }
    }

    public String getPrice(){
        if (productCurrentPrice.count() > 0){
            return productCurrentPrice.innerText();
        }
        else if(productPriceAsConfigured.count() > 0){
            return productPriceAsConfigured.innerText();
        }
        else{
            return null;
        }

    }

    public String getProductName(){
        return productName.textContent();
    }


    public void navigateToProductPage(String productUrl) {
        page.navigate(productUrl);
    }

    public void setQuantity(String quantity) {
        if (quantityInput.isVisible()) {
            quantityInput.fill(quantity);
        }
    }


    public void clickAddToCartButton() {
        addToCartButton.click();
    }

    public void clickAddToWishlistButton() {
        addToWishlistButton.click();
    }

    public void clickAddToCompareButton() {
        addToCompareButton.click();
    }

    public int dropdownsCount() {
        return dropdowns.count();
    }



    public void selectAvailableMandatoryAttributes() {
        selectAvailableMandatorySwatchAttributes();
        selectAvailableMandatoryDropdownAttributes();
    }

    public void selectAvailableOptionalAttributes() {
        selectAvailableOptionalSwatchAttributes();
        selectAvailableOptionalDropdownAttributes();
    }

    private void selectAvailableMandatorySwatchAttributes() {
        try {
            SelectAvailableSwatchAttributes(swatchGroups);
        } catch (Exception e) {
            System.out.println("No mandatory swatch attributes found");
        }
    }



    private void selectAvailableOptionalSwatchAttributes() {
        try {
            SelectAvailableSwatchAttributes(optionalSwatchGroups);
        } catch (Exception e) {
            System.out.println("No optional swatch attributes found");
        }
    }

    private void SelectAvailableSwatchAttributes(Locator SwatchGroups) {
        if(SwatchGroups.count() > 0) {
            for (int i = 0; i < SwatchGroups.count(); i++) {
                Locator swatchGroup = SwatchGroups.nth(i);
                Locator validOptions = swatchGroup.locator("li:not(.not-available)");
                for (int j = 0; j < validOptions.count(); j++) {
                    Locator option = validOptions.nth(j);
                    option.click();
                    Locator outOfStockButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("OUT OF STOCK"));
                    if (!outOfStockButton.isVisible()) {
                        break;
                    }
                }
            }
        }
    }


    private void selectAvailableMandatoryDropdownAttributes() {
        try {
            SelectAvailableDropdownAttributes(mandatoryDropdowns);
        } catch (Exception e) {
            System.out.println("No mandatory dropdown attributes found");
        }
    }

    private void selectAvailableOptionalDropdownAttributes() {
        try {
            SelectAvailableDropdownAttributes(optionalDropdowns);
        } catch (Exception e) {
            System.out.println("No optional dropdown attributes found");
        }
    }

    private void SelectAvailableDropdownAttributes(Locator Dropdowns) {
        if (Dropdowns.count() > 0) {
            for (int i = 0; i < Dropdowns.count(); i++) {
                Locator dropdown = Dropdowns.nth(i);
                dropdown.click();

                List<String> valueList = new ArrayList<>();
                Locator options = dropdown.locator("option");

                for (int j = 0; j < options.count(); j++) {
                    Locator option = options.nth(j);
                    String optionText = option.textContent().trim();
                    String optionValue = option.getAttribute("value");
                    String optionClass = option.getAttribute("class");

                    if(optionText.matches(".*Choose.* | .*Select.*") ||
                            optionValue == null || optionValue.isEmpty() ||
                            (optionClass != null && optionClass.contains("not-available"))) continue;

                    valueList.add(optionValue);
                }

                for (String value : valueList) {
                    dropdown.selectOption(new SelectOption().setValue(value));
                    Locator outOfStock = page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("OUT OF STOCK"));
                    if (outOfStock.isVisible()) continue;
                    break;
                }
            }
        }
    }





}