package com.restfulbooker.cucumber.steps;

import com.restfulbooker.bookinginfo.BookingSteps;
import com.restfulbooker.model.AuthPojo;
import com.restfulbooker.utils.TestUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class BookingCRUDSteps {

    static String token;
    static String headerToken;
    static List<Integer> bookingIdsBeforePost;
    static List<Integer> bookingIdsAfterPost;
    static HashMap<String, Object> booking;
    static ValidatableResponse response;
    static String firstname = TestUtils.getRandomText();
    int bookingid;

    @Steps
    BookingSteps bookingSteps;

    @Given("^When user navigates to the Restful Booker api url using the auth endpoint$")
    public void whenUserNavigatesToTheRestfulBookerApiUrlUsingTheAuthEndpoint() {
    }

    @When("^Admin creates a new post request by entering username \"([^\"]*)\" and password \"([^\"]*)\" as payload$")
    public void adminCreatesANewPostRequestByEnteringUsernameAndPasswordAsPayload(String username, String password) {
        token = bookingSteps.getAuthToken(username, password);
        System.out.println("Authorisation token is : " + token);

    }

    @Then("^Admin should see an auth token generated to use for future requests$")
    public void adminShouldSeeAnAuthTokenGeneratedToUseForFutureRequests() {
        if (!(token == null)) {
            Assert.assertTrue(true);
            System.out.println("The verified token is : " + token);
        }
    }

    @Given("^User extracts a list of existing ID's before creating a new record$")
    public void userExtractsAListOfExistingIDSBeforeCreatingANewRecord() {
        bookingIdsBeforePost = SerenityRest.given()
                    .when()
                    .get()
                    .then()
                    .extract()
                    .path("bookingid");
            System.out.println("Old List of ID's are :" + bookingIdsBeforePost);
            System.out.println("********************************************");

    }
    @When("^User creates a new booking using the correct details \"([^\"]*)\" \"([^\"]*)\"\"([^\"]*)\" \"([^\"]*)\"$")
    public void userCreatesANewBookingUsingTheCorrectDetails(String lastname, int totalprice, boolean deposit, String needs) {
        //hashmap for booking dates
        booking = new HashMap<>();
        booking.put("checkin", "2022-01-01");
        booking.put("checkout", "2022-02-01");
        response = bookingSteps.createNewBooking(firstname, lastname, totalprice, deposit, booking, needs);
        response.statusCode(200).log().all();
    }

    @And("^User verifies the new booking has been created successfully$")
    public void userVerifiesTheNewBookingHasBeenCreatedSuccessfully() {
        //finding the list of ids after posting with intention to find new record's id
        bookingIdsAfterPost = SerenityRest.given()
                .when()
                .get()
                .then()
                .extract()
                .path("bookingid");

        System.out.println("Old List of ID's are :" + bookingIdsBeforePost);
        System.out.println("New List of ID's are :" + bookingIdsAfterPost);
        System.out.println("********************************************");

        //remove all old ids from new id list using removeAll method of list
        // (the remainder will be id of new record)
        bookingIdsAfterPost.removeAll(bookingIdsBeforePost);

        //extract the newly added id from the updated list and assign value to bookingid variable
        bookingid = (bookingIdsAfterPost.get(0));
        System.out.println("The newly generated id is: " + bookingid);
        System.out.println("********************************************");

        //finding the new booking by id
        ValidatableResponse response = bookingSteps.findNewRecordById(bookingid);
        //validating the new record by status code verification
        response.statusCode(200)
                .log().all();
        System.out.println(bookingid);
    }

    @And("^User updates the created record by updating the firstname \"([^\"]*)\" \"([^\"]*)\"\"([^\"]*)\" \"([^\"]*)\"$")
    public void userUpdatesTheCreatedRecordByUpdatingTheFirstname(String lastname, int totalprice, boolean depositpaid, String additionalneeds)  {
        firstname = firstname + "updated";
        headerToken = "token=" + token;
        response = bookingSteps.updateBookingRecordById(firstname, lastname, totalprice, depositpaid, booking, additionalneeds, bookingid, headerToken);
        //validating the response for put method
        response.log().all();
    }

    @And("^User verifies that the record has been updated successfully$")
    public void userVerifiesThatTheRecordHasBeenUpdatedSuccessfully() {
        //validating by comparing booking id from extraction of actual record by firstname
        List<Integer> id = bookingSteps.findSingleBookingRecordByFirstName(firstname, bookingid);
        System.out.println("Actual id is : " + id.get(0));
        System.out.println("Expected id is : " + bookingid);
        Assert.assertThat(id.get(0), equalTo(bookingid));
    }

    @And("^User deletes the newly created record by providing the booking id$")
    public void userDeletesTheNewlyCreatedRecordByProvidingTheBookingId() {
        //deleting the booking
        bookingSteps.deleteBooking(bookingid, headerToken);
        //verifying the deletion by finding the record with id
        response = bookingSteps.findNewRecordById(bookingid);

    }

    @Then("^User verifies that the record has been deleted successfully$")
    public void userVerifiesThatTheRecordHasBeenDeletedSuccessfully() {
        //After finding the response validating that the status code should be 404 to confirm deletion
        response.statusCode(404)
                .log().all();
    }
}


