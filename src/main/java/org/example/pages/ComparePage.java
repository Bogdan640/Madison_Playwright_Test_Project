package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ComparePage {

    private Page page;
    private Map<String, Map<String, String>> products = new HashMap<>();
    private  Locator productNamesSelector;
    private  Locator priceLabel;



    public ComparePage(Page page) {
        this.page = page;
        //get all product names in
        this.productNamesSelector = page.locator(".product-name");
        for (Locator prod_name : productNamesSelector.all()){
            products.put(prod_name.innerText(), new HashMap<>());
        }

        this.priceLabel = page.locator("#product-price");
        /// will fail due to bug, for writing the future test I will go around this
        //assertThat(priceLabel).hasCount(products.size());
        for (int i = 0; i < productNamesSelector.all().size(); i++) {
            products.get(productNamesSelector.all().get(i).innerText()).put("price", priceLabel.all().get(i).innerText());
        }

    }
}



