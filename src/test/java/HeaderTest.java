import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String navHeadline = "Women";
        this.headerPage.clickNavigationHeadline(navHeadline);
        assertThat(page).hasTitle(navHeadline);
    }

    @Test
    void testHoverNavigationHeadline() {
        this.headerPage.navigate();
        String navHeadline = "Women";
        this.headerPage.hoverNavigationHeadline(navHeadline);
        assertTrue(this.headerPage.getVisibleNavDropdownContent().contains(navHeadline));
    }
}
