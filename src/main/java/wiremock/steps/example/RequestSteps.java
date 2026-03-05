package wiremock.steps.example;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class RequestSteps {

    @Step("POST {baseUrl}{path}")
    public ValidatableResponse post(String baseUrl, String path, String body) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(baseUrl)
                .body(body)
                .when()
                .post(path)
                .then();
    }
}
