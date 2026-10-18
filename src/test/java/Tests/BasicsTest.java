package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {

    Page page;

    @BeforeMethod
    public void setup() {
        Playwright playwright  = Playwright.create();
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

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        page.locator("#event-title-input").fill("MSFTSrep");
        page.locator("#admin-event-form textarea").fill("MSFTSrep summit");
        page.locator("#city").fill("Bangalore");
        page.getByLabel("Category").selectOption("Concert");
        page.locator("#venue").fill("Shivaji nagar");
        page.getByLabel("Event Date & Time").fill("2026-10-25T07:30");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("50");
        page.locator("#add-event-btn").click();

        assertThat(page.getByText("Event created!")).isVisible();

        page.locator("nav a[href='/events']").click();
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        System.out.println(eventCards.count());
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("MSFTSrep"));
        assertThat(targetCard).isVisible();

        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int seatsNumBeforeBooking  =  Integer.parseInt(seatsText.split(" ")[0]);
        targetCard.getByTestId("book-now-btn").click(new Locator.ClickOptions().setTimeout(5000));

        //Booking
        page.getByPlaceholder("Your full name").fill("Rahul");
        page.getByPlaceholder("you@email.com").fill("rahul12@gmail.com");
        page.locator("#phone").fill("1234567890");
        page.locator("#confirm-booking").click(new  Locator.ClickOptions().setTimeout(7000));
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //Verify in Booking History
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new  Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();

        //Seat Count Reduction
        page.locator("nav a[href='/events']").click();
        page.waitForTimeout(1000);

        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("MSFTSrep"));
        assertThat(targetCardAfterBooking).isVisible();

        String seatsText2 = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsText2);

        int seatsNumAfterBooking  =  Integer.parseInt(seatsText2.split(" ")[0]);

        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);


    }
}
