package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashBoardPage {

    Page page;

    public  DashBoardPage(Page page) {
        this.page = page;
    }

    public void waitForPageToLoad(){
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

    }
}
