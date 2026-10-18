package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    String eventTitle = "#event-title-input";
    String eventDescription = "#admin-event-form textarea";
    String eventCity = "#city";
    String eventCategory = "Category";
    String eventVenue = "#venue";
    String eventDateTime = "Event Date & Time";
    String eventPrice = "Price ($)";
    String eventTotalSeats = "Total Seats";


    public AdminEventsPage(Page page){
        this.page = page;
    }

    public void goToAdminEventsPage() {
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void actionEventsPage(){
        page.locator(eventTitle).fill("MSFTSrep");
        page.locator(eventDescription).fill("MSFTSrep summit");
        page.locator(eventCity).fill("Bangalore");
        page.getByLabel(eventCategory).selectOption("Concert");
        page.locator(eventVenue).fill("Shivaji nagar");
        page.getByLabel(eventDateTime).fill("2026-10-25T07:30");
        page.getByLabel(eventPrice).fill("100");
        page.getByLabel(eventTotalSeats).fill("50");
        page.locator("#add-event-btn").click();

        assertThat(page.getByText("Event created!")).isVisible();
    }

}
