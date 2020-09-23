package setup.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

public class SimpleWireMockTestExample extends BaseTest {

    @Test
    public void simple_wiremock_test()  {
        given()
                .baseUri("http://localhost:8282")
                .body("{\"someparam\": \"someValue\"}")
        .when()
                .post("/simple_test_url/v1")
        .then()
                .statusCode(200);
    }

    @Test
    public void simple_wiremock_test_with_body()  {
        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("success"))
                .build();

        given()
                .baseUri("http://localhost:8282")
                .body("{\"someparam\": \"someValue\"}")
        .when()
                .post("/simple_test_response_body_url/v1")
        .then()
                .spec(responseSpec);
    }

}
