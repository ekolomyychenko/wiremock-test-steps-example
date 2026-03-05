package wiremock.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringContains.containsString;

public class SimpleWireMockTest extends BaseTest {

    private final RequestSteps steps = new RequestSteps();

    @Test
    @DisplayName("Пример простой заглушки без тела ответа")
    public void simple_wiremock_test() {
        steps.post(getBaseUrl(), "/simple_test_url/v1", "{\"someParam\": \"someValue\"}")
                .statusCode(200);
    }

    @Test
    @DisplayName("Пример простой заглушки с телом ответа")
    public void simple_wiremock_test_with_body() {
        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("success"))
                .build();

        steps.post(getBaseUrl(), "/simple_test_response_body_url/v1", "{\"someParam\": \"someValue\"}")
                .spec(responseSpec);
    }
}
