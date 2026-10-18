package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    Page page;

    @BeforeMethod
    public void setup() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }

    @Test
    public void testBasics() {
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //page.getByLabel("Email").fill("johnDoe@gmail.com");
        page.getByPlaceholder("you@email.com").fill("johndeer@gmail.com");
        page.getByLabel("Password").fill("John@145");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCard = page.getByTestId("event-card");

        assertThat(eventCard.first()).isVisible();

        Assert.assertEquals(eventCard.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))
        ));

        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCard1 = page.getByTestId("event-card");

        assertThat(eventCard1.first()).isVisible();

        Assert.assertEquals(eventCard1.count(),4 );
        assertThat(page.locator(".mx-1").first()).isHidden();



    }

    @Test
    public void  routeResumeTest(){
        //root.resume
        page.getByPlaceholder("you@email.com").fill("johndeer@gmail.com");
        page.getByLabel("Password").fill("John@145");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


        page.getByTestId("nav-bookings").click();

        page.route("**/api/bookings/**", route -> route.resume(
                new Route.ResumeOptions().setUrl("\n" +
                        "https://api.eventhub.rahulshettyacademy.com/api/bookings/31313")
        ));

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

        assertThat(page.getByText("Access Denied")).isVisible();

    }

}
