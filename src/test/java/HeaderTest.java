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

import java.util.List;

public class HeaderTest extends BaseTest{



    @Test
    void testLogo() {
        headerPage.navigate();
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
        String input = "shirt";
        this.headerPage.search(input);
        assertThat(page).hasTitle("Search results for: '" + input + "'");
    }

    @Test
    void testClickNavigationHeadlines() {
        this.headerPage.navigate();
        List<String> navHeadlines = this.headerPage.getNavigationNames();
        for (String nav : navHeadlines) {
            this.headerPage.clickNavigationHeadline(nav);
            assertThat(page).hasTitle(nav);
        }
    }

    @Test
    void testHoverNavigationHeadline() {
        this.headerPage.navigate();
        List<String> navHeadlines = this.headerPage.getHoverableNavigationNames();
        for (String nav : navHeadlines) {
            this.headerPage.hoverNavigationHeadline(nav);
            assertTrue(this.headerPage.getVisibleNavDropdownContent().contains(nav));
        }
    }


}
