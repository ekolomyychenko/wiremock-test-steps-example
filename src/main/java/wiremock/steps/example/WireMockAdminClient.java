package wiremock.steps.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.net.HttpHeaders;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.NonNull;

import java.io.File;

public class WireMockAdminClient {

    private static final String REQUESTS_URL = "/__admin/requests";
    private static final String FIND_URL = "/__admin/requests/find";
    private static final String UNMATCHED_URL = "/__admin/requests/unmatched";
    private static final String COUNT_URL = "/__admin/requests/count";

    private final String baseUrl;

    public WireMockAdminClient(WireMockServer server) {
        this.baseUrl = "http://localhost:" + server.port();
    }

    public Response getAllRequests() {
        return RestAssured.get(baseUrl + REQUESTS_URL);
    }

    public Response findRequests(@NonNull File requestJson) {
        return RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(requestJson)
                .post(baseUrl + FIND_URL);
    }

    public Response getUnmatchedRequests() {
        return RestAssured.get(baseUrl + UNMATCHED_URL);
    }

    public Response getMatchedCount(@NonNull File requestJson) {
        return RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(requestJson)
                .post(baseUrl + COUNT_URL);
    }

    public void deleteAllRequests() {
        RestAssured.delete(baseUrl + REQUESTS_URL);
    }
}
