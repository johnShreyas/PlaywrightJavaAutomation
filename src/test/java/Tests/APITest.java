package Tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {
    @Test
    public void testAPI() {

        //login
        HashMap<Object, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "johndeer@gmail.com");
        loginPayload.put("password", "John@145");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());

        String token = JsonPath.read(loginResponse.text(), "$.token");
        System.out.println(token);

        //Create Event
        String eventTitle = "PlayWright API Test";
        HashMap<Object, Object> createEventPayload = new HashMap<>();

        createEventPayload.put("title", eventTitle);
        createEventPayload.put( "description", "API details");
        createEventPayload.put("category", "Sports");
        createEventPayload.put( "venue", "Shivajinagar");
        createEventPayload.put("city", "Bangalore");
        createEventPayload.put("eventDate", "2026-10-31T08:17:00.000Z");
        createEventPayload.put( "price", 100);
        createEventPayload.put("totalSeats", 400);

        APIResponse createEventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token)
                        .setData(createEventPayload));
        Assert.assertTrue(createEventResponse.ok());
        int eventId = JsonPath.read(createEventResponse.text(), "$.data.id");

        System.out.println(eventId + "Event Created...");

        //Get Event
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events?page=1&limit=12",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token));

        Assert.assertTrue(retrieveEvents.ok(), "Retrieve Event API successful");
        System.out.println(retrieveEvents.text());

        List<Integer> EventIds = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(EventIds.contains(eventId), "Created Event");

        //Delete Event
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/" + eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer " + token));

        Assert.assertTrue(deleteResponse.ok(), "Delete Event API successful");
        System.out.println(deleteResponse.text());

        //Verify Deletion is success
        APIResponse verifyRes = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token));

        List<String> idAfterDelete =   JsonPath.read(verifyRes.text(), "$.data[*].id");
        Assert.assertFalse(idAfterDelete.contains(eventId), "Deleted Event");

        System.out.println("Delete Event API successful");


    }
}
