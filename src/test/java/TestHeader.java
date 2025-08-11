import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.example.pages.HeaderPage;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHeader {
    static Playwright playwright;
    static Browser browser;
    Page page;
    HeaderPage headerPage;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch();
    }

    @BeforeEach 
    void setupTest() {
        BrowserContext context = browser.newContext();
        page = context.newPage();
        this.headerPage = new HeaderPage(page);
    }

    @Test
    void testLogo() {
        this.headerPage.navigate();
        this.headerPage.clickLogo();
        assertThat(page).hasURL("http://qa3magento.dev.evozon.com/");
    }

    @Test
    void testAccountButton() {
        this.headerPage.navigate();
        this.headerPage.clickAccountButton();
        assertEquals(this.headerPage.isAccountDetailsVisible(), true);
    }

    @Test
    void testCartButton() {
        this.headerPage.navigate();
        this.headerPage.clickCartButton();
        assertEquals(this.headerPage.isCartDetailsVisible(), true);
    }

    @Test
    void testSearchBar() {
        this.headerPage.navigate();
        this.headerPage.search("shirt");
        assertThat(page).hasTitle("Search results for: 'shirt'");
    }

    @Test
    void testClickNavigationHeadline() {
        this.headerPage.navigate();
        this.headerPage.clickNavigationHeadline("Women");
        assertThat(page).hasTitle("Women");
    }

    @Test
    void testHoverNavigationHeadline() {
        this.headerPage.navigate();
        String sectionName = "Home & Decor";
        this.headerPage.hoverNavigationHeadline(sectionName);
        assertTrue(this.headerPage.getVisibleNavDropdownContent(sectionName).contains(sectionName));
    }

    @AfterAll 
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}
