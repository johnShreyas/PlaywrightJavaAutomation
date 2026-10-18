package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MoreUIValidationsTest {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");

    }

    @Test
    public void ChildWindowHandle() {
        Locator blinkingText = page.locator(".blinkingText");

        Page newpage = context.waitForPage(() -> blinkingText.first().click());
        newpage.waitForLoadState();
        String childText = newpage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        // System.out.println("emailId: " + emailId);
        page.getByLabel("Username").fill(emailId);
        page.waitForTimeout(3000);
        System.out.println(page.getByLabel("Username").inputValue());
    }

    @Test
    public void UiControls() {

        Locator userBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userBtn.click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userBtn.isChecked());

        Locator checkBoxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));

        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");

        page.waitForTimeout(3000);
    }
}
