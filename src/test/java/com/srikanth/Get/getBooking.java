package com.srikanth.Get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class getBooking {

    @Test
    public void getBookingId(){

        // Method : GET
        // URL: https://restful-booker.herokuapp.com/booking/:id
        // Header  Accept: String
        RestAssured
                .given().log().all()
                    .baseUri("https://restful-booker.herokuapp.com/")
                    .basePath("booking/2")
                .when().log().all()
                    .get()
                .then().log().all()
                    .statusCode(200);
    }

    @Test
    public void getInvalidId(){
        RestAssured
                .given().log().all()
                .baseUri("https://restful-booker.herokuapp.com/")
                .basePath("booking/String")
                .when().log().all()
                .get()
                .then().log().all()
                .statusCode(404);

    }

@Test
    public void createBooking(){

        String payload = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        RestAssured
                .given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking")
                    .contentType(ContentType.JSON)
                    .body(payload)
                .when().log().all()
                    .post()
                .then().log().all()
                    .statusCode(200);

    }

}
