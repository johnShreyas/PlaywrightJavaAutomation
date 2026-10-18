package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    Page page;

    public EventsPage(Page page) {
        this.page = page;
    }

    public void goToEventsPage(){
        page.locator("nav a[href='/events']").click();
    }

    public Locator waitForEventsToLoad(){
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(){
        Locator eventCards = waitForEventsToLoad();
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("MSFTSrep"));
        assertThat(targetCard).isVisible();
        return targetCard;
    }

    public int getSeatsCount(Locator targetCard){

        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);
    }

    public BookingFormPage proceedToBooking(Locator targetCard){

        targetCard.getByTestId("book-now-btn").click(new Locator.ClickOptions().setTimeout(5000));
        return new BookingFormPage(page);
    }



}
