package wiremock.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.StringContains.containsString;

public class ResponseTemplatingWireMockTest extends BaseTest {

    private static final RequestSteps steps = new RequestSteps();

    @Test
    @DisplayName("Пример подстановки в ответ от wiremock данных на лету — это может быть переменная из контекста или сессии, которая не известна заранее")
    public void response_templating_wiremock_test() {
        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody(containsString("lol kek"))
                .build();

        steps.post(getBaseUrl(), "/response_templating_test_url/v1", "{\"someParam\": \"someValue\"}")
                .spec(responseSpec);
    }
}
