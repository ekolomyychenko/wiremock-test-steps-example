package wiremock.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringContains.containsString;

public class DifferentResponseForSecondRequestWireMockTest extends BaseTest {

    private static final RequestSteps steps = new RequestSteps();

    @Test
    @DisplayName("Пример разного ответа от wiremock на одинаковые запросы, в зависимости от номера запроса")
    public void different_response_for_second_request_wiremock_test() {
        // 1-st request
        ResponseSpecification responseSpecFirstRequest = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("First Request"))
                .build();

        steps.post(getBaseUrl(), "/url/v1/different/stubs", "{\"someParam\": \"someValue\"}")
                .spec(responseSpecFirstRequest);

        // 2-d request
        ResponseSpecification responseSpecSecondRequest = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("Second Request"))
                .build();

        steps.post(getBaseUrl(), "/url/v1/different/stubs", "{\"someParam\": \"someValue\"}")
                .spec(responseSpecSecondRequest);
    }
}
