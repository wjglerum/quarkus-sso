package com.lunatech;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class BaseResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when()
                .redirects().follow(false)
                .get("/hello")
                .then()
                .statusCode(302);
    }

    @Test
    @TestSecurity(user = "alice", roles = { "user", "admin" })
    public void testAdminEndpoint() {
        given()
                .when()
                .get("/hello/admin")
                .then()
                .statusCode(200)
                .body(containsString("alice"));
    }

    @Test
    @TestSecurity(user = "bob", roles = { "user" })
    public void testAdminEndpointRejected() {
        given()
                .when()
                .get("/hello/admin")
                .then()
                .statusCode(403)
                .body(is(""));
    }

    @Test
    @TestSecurity(user = "bob", roles = { "user" })
    public void testUserEndpoint() {
        given()
                .when()
                .get("/hello/user")
                .then()
                .statusCode(200)
                .body(containsString("bob"));
    }

    @Test
    @TestSecurity(user = "alice", roles = {})
    public void testUserEndpointRejected() {
        given()
                .when()
                .get("/hello/user")
                .then()
                .statusCode(403)
                .body(is(""));
    }
}
