package com.srikanth.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class IntegrationWithRestFullBooker {

    RequestSpecification requestSpecification = RestAssured.given();
    Response response;
    ValidatableResponse validatableResponse;

    // Create a Token
    // Create a Booking
    // Perform  a PUT request
    // Verify that PUT is success by GET Request
    // Delete the ID
    // Verify it doesn't exist GET Request
    String token;
    String bookingId;

    public String getToken() {

        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/auth");
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.body(payload).log().all();

        Response response = requestSpecification.when().post();

        ValidatableResponse validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        // Extract token
        String token = response.jsonPath().getString("token");
        System.out.println(token);
        return token;
    }

    public String getBookingID() {
        String payload_POST = "{\n" +
                "    \"firstname\" : \"Pramod\",\n" +
                "    \"lastname\" : \"Dutta\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2024-01-01\",\n" +
                "        \"checkout\" : \"2024-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking");
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.body(payload_POST).log().all();

        response = requestSpecification.when().post();

        // Get Validatable response to perform validation
        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

        bookingId = response.jsonPath().getString("bookingid");
        System.out.println(bookingId);

        //  assertThat(bookingId).isNotBlank().isNotEmpty().isNotNull()

        return bookingId;
    }


    @Test(priority = 1)
    public void test_update_request_put() {
        token = getToken();
        bookingId= getBookingID();
        System.out.println(token);
        System.out.println(bookingId);


        String payloadPUT= "{\n" +
                "    \"firstname\" : \"Anshul\",\n" +
                "    \"lastname\" : \"Ji\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2024-01-01\",\n" +
                "        \"checkout\" : \"2024-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";


        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);
        requestSpecification.body(payloadPUT).log().all();

        response = requestSpecification.when().put();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);

    }

    @Test(priority = 2)
    public void test_update_request_get() {
        System.out.println(bookingId);
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingId);
        response = requestSpecification.when().log().all().get();
        requestSpecification.then().log().all().statusCode(200);

        String firstname = response.jsonPath().getString("firstname");
        //Assert.assertEquals(firstname,"Anshul");


    }

    @Test(priority = 3)
    public void test_delete_booking() {
        System.out.println(bookingId);
        System.out.println(token);



        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingId);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("token",token);

        response = requestSpecification.when().delete();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(201); // #TODO #1 -Delete Bug

    }

    @Test(priority = 4)
    public void test_after_delete_request_get() {
        requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
        requestSpecification.basePath("/booking/"+bookingId);
        response = requestSpecification.when().log().all().get();
        requestSpecification.then().log().all().statusCode(404);

    }


}
