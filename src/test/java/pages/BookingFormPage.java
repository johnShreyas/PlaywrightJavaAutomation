package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {

    Page page;

    String FullName = "Your full name";
    String CustomerEmail = "you@email.com";
    String PhonePlaceholder = "#phone";
    String ConfirmButton = "#confirm-booking";

    public BookingFormPage(Page page) {
        this.page = page;
    }


    public void fillAndConfirm(String name, String email, String phone){

        page.getByPlaceholder(FullName).fill(name);
        page.getByPlaceholder(CustomerEmail).fill(email);
        page.locator(PhonePlaceholder).fill(phone);
        page.locator(ConfirmButton).click(new  Locator.ClickOptions().setTimeout(7000));
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();

    }

}
