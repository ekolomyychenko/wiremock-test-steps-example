package wiremock.steps.example;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

public class DifferentResponseForSecondRequestWireMockTestExample extends BaseTest {

	@Test
	@DisplayName("Пример разного ответа от wiremock на одинаковые запросы, в зависимости от номера запроса")
	public void different_response_for_second_request_wiremock_test() {
		// 1-st request
		ResponseSpecification responseSpecFirstRequest = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectBody(containsString("First Request"))
				.build();

		given()
				.baseUri("http://localhost:8282")
				.body("{\"someParam\": \"someValue\"}")
				.when()
				.post("/url/v1/different/stubs")
				.then()
				.spec(responseSpecFirstRequest);

		// 2-d request
		ResponseSpecification responseSpecSecondRequest = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectBody(containsString("Second Request"))
				.build();

		given()
				.baseUri("http://localhost:8282")
				.body("{\"someParam\": \"someValue\"}")
				.when()
				.post("/url/v1/different/stubs")
				.then()
				.spec(responseSpecSecondRequest);
	}
}
