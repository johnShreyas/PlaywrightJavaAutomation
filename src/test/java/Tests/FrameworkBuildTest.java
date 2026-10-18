package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase{

    @Test
    public void testBasics() {

        LoginPage loginPage = new LoginPage(page, base_url);
        DashBoardPage dashBoardPage = loginPage.loginToApplication();
        dashBoardPage.waitForPageToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goToAdminEventsPage();
        adminEventsPage.actionEventsPage();

        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goToEventsPage();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard();
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBooking(targetCard);
        bookingFormPage.fillAndConfirm(
                "Test Student",
                "test.student@gmail.com",
                "1235751245"
        );


        //Booking

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
