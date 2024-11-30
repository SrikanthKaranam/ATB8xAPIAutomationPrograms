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

}
