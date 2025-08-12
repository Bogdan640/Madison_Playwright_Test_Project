package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.util.Collections.replaceAll;

public class ProductPage {
    private Page page;
    private final Locator quantityInput;
    private final Locator addToCartButton;
    private final Locator addToWishlistButton;
    private final Locator addToCompareButton;

    private final Locator swatchGroups;
    private final Locator optionalSwatchGroups;

    private final Locator dropdowns;
    private final Locator dropdowns_type_2;
    private final Locator mandatoryDropdowns;
    private final Locator optionalDropdowns;


    private final Locator textAreaAttribute;

    //price
    private final Locator productCurrentPrice;
    private final Locator productPriceAsConfigured;
    private final Locator productStartingPrice;
    private final Locator productMaximumPrice;


    //success message for add to compare list
    private final Locator getAddToCompareSuccessMsg;

    //current product attribute list
    Map<String, String> selectedAttributes = new HashMap<>();





    private final Locator productName;

    public ProductPage(Page page) {
        this.page = page;
        this.quantityInput = page.locator("#qty");
        //buttons
        this.addToCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
        this.addToCompareButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Add to Compare"));
        this.addToWishlistButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Add to Wishlist"));

        //attributes

        //Swatches
        this.optionalSwatchGroups = page.locator("#product-options-wrapper select:not(.required-entry)~ul[id *='swatch']");
        this.swatchGroups = page.locator("#product-options-wrapper ul[id *='swatch']");

        //dropdowns
        this.dropdowns = page.getByText("Choose a selection...");
        this.dropdowns_type_2 = page.getByText("PLease Select");
        this.mandatoryDropdowns = page.locator("#product-options-wrapper select.required-entry[id*='bundle']");
        this.optionalDropdowns = page.locator("#product-options-wrapper dd select:not(.required-entry)");

        //textArea

        this.textAreaAttribute = page.locator("textarea[id ^= 'option']");


        //price
        this.productCurrentPrice = page.locator("[id^=product-price] .price").first();
        this.productPriceAsConfigured = page.getByText("Price as configured:");
        this.productStartingPrice = page.getByText("From:");
        this.productMaximumPrice = page.getByText("To:");


        this.getAddToCompareSuccessMsg = page.getByText("has been added to comparison list");

        this.productName = page.locator(".product-shop .product-name");
    }


    public Map<String, String> getSelectedAttributes() {
        return selectedAttributes;
    }


    public boolean hasDropdowns() {
        return dropdowns.count() + dropdowns_type_2.count() > 0;
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
        assertThat(getAddToCompareSuccessMsg).isVisible();
    }

    public int dropdownsCount() {
        return dropdowns.count();
    }


    private void selectTextAreaAttribute() {
        try{
            if (textAreaAttribute.isVisible() && textAreaAttribute.count() > 0) {
                textAreaAttribute.fill("Some sample text");
            }
        }catch (Exception e){
            System.out.println("There is no text area attribute");
        }
    }


    public void selectAvailableMandatoryAttributes() {
        selectAvailableMandatorySwatchAttributes();
        selectAvailableMandatoryDropdownAttributes();
    }

    public void selectAvailableOptionalAttributes() {
        selectAvailableOptionalSwatchAttributes();
        selectAvailableOptionalDropdownAttributes();
        selectTextAreaAttribute();
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

    private void SelectAvailableSwatchAttributes(Locator swatchGroups) {
        if (swatchGroups.count() > 0) {
            for (int i = 0; i < swatchGroups.count(); i++) {
                Locator swatchGroup = swatchGroups.nth(i);

                String labelText = "";
                try {
                    labelText = swatchGroup
                            .locator("xpath=preceding::dt[1]/label")
                            .first()
                            .innerText()
                            .replace("*", "")
                            .replaceAll("\\s+", " ")
                            .replaceAll(":", "")
                            .trim();
                } catch (Exception e) {
                    continue;
                }

                // Skip if already selected this attribute
                if (selectedAttributes.containsKey(labelText)) {
                    continue;
                }

                // Get all available options for this swatch
                Locator validOptions = swatchGroup.locator("li:not(.not-available)");

                for (int j = 0; j < validOptions.count(); j++) {
                    Locator option = validOptions.nth(j);

                    // Get the option name
                    Locator swatchNameLocator = option.locator(">a");
                    String swatchName = swatchNameLocator.getAttribute("title");
                    if (swatchName == null || swatchName.isEmpty()) {
                        swatchName = swatchNameLocator.innerText().trim();
                    }
                    swatchName = swatchName.replaceAll("\\s+", " ").trim();

                    option.click();
                    Locator outOfStockButton = page.getByRole(
                            AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("OUT OF STOCK")
                    );

                    if (!outOfStockButton.isVisible()) {
                        selectedAttributes.put(labelText, swatchName);
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
            SelectAvailableDropdownAttributes(dropdowns_type_2);
        } catch (Exception e) {
            System.out.println("No optional dropdown attributes found");
        }
    }

    private void SelectAvailableDropdownAttributes(Locator Dropdowns) {
        if (Dropdowns.count() > 0) {
            for (int i = 0; i < Dropdowns.count(); i++) {
                Locator dropdown = Dropdowns.nth(i);

                // Get label text
                String labelText = dropdown
                        .locator("xpath=ancestor::dd/preceding-sibling::dt[1]/label")
                        .textContent()
                        .trim()
                        .replace("*", "")
                        .replaceAll("\\s+", " ")
                        .replaceAll(":", "")
                        .trim();

                dropdown.click();

                List<Locator> validOptions = new ArrayList<>();
                Locator options = dropdown.locator("option");

                for (int j = 0; j < options.count(); j++) {
                    Locator option = options.nth(j);
                    String optionText = option.textContent().trim();
                    String optionValue = option.getAttribute("value");
                    String optionClass = option.getAttribute("class");

                    if (optionText.matches(".*Choose.*|.*Select.*") ||
                            optionValue == null || optionValue.isEmpty() ||
                            (optionClass != null && optionClass.contains("not-available"))) {
                        continue;
                    }

                    validOptions.add(option);
                }

                // Pick the first available option
                for (Locator option : validOptions) {
                    String valueAttr = option.getAttribute("value");
                    String textAttr  = option.textContent().trim();

                    dropdown.selectOption(new SelectOption().setValue(valueAttr));

                    Locator outOfStock = page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("OUT OF STOCK"));
                    if (outOfStock.isVisible()) continue;

                    // Locate the quantity input related to this dropdown
                    Locator qtyInput = dropdown.locator("xpath=following::input[contains(@id,'-qty-input')][1]");
                    String qtyValue = "1"; // default quantity
                    if (qtyInput.count() > 0) {
                        String rawQty = qtyInput.inputValue().trim();
                        if (!rawQty.isEmpty()) {
                            qtyValue = rawQty;
                        }
                    }

                    // Store human-readable text with quantity appended
                    selectedAttributes.put(labelText, qtyValue + " x " + textAttr);
                    break;
                }
            }
        }
    }

}







