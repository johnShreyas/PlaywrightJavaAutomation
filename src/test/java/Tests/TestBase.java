package Tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {

    Page page;
    Playwright playwright;
    Browser browser;
    String base_url;

    @BeforeMethod
    public void setup() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        String browserName  = prop.getProperty("browser");
        playwright  = Playwright.create();

        if("firefox".equals(browserName)){
            browser = playwright.firefox().launch();
        } else if ("safari".equals(browserName)) {
            browser = playwright.webkit().launch();
        } else {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        }

        page = browser.newPage();
        base_url = prop.getProperty("qa.base_url");


    }

}
