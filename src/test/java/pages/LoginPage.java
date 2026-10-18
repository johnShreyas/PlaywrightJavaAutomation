package pages;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    Page page;
    String base_url;
    String email_placeHolder = "you@email.com";
    String password_placeholder = "Password";


    public DashBoardPage loginToApplication() {
        page.navigate(base_url);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //page.getByLabel("Email").fill("johnDoe@gmail.com");
        page.getByPlaceholder(email_placeHolder).fill("johndeer@gmail.com");
        page.getByLabel(password_placeholder).fill("John@145");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashBoardPage dashBoardPage = new DashBoardPage(page);
        return dashBoardPage;
    }

    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.base_url = baseUrl;
    }
}
