package wiremock.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

public class ResponseTemplatingWireMockTestExample extends BaseTest {

    @Test
    @DisplayName("Пример подстановки в ответ от wiremock данных на лету — это может быть переменная из контекста или сессии, которая не известна заранее")
    public void response_templating_wiremock_test() {
        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("lol kek"))
                .build();

        given()
                .baseUri("http://localhost:8282")
                .body("{\"someParam\": \"someValue\"}")
        .when()
                .post("/response_templating_test_url/v1")
        .then()
                .spec(responseSpec);
    }
}
