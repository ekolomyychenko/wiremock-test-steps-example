package wiremock.steps.example;


import com.google.common.net.HttpHeaders;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Log4j2
public class WireMockClient {

    private final String WIRE_MOCK_URL = "/__admin/requests";
    private final String WIRE_MOCK_FIND_URL = "/__admin/requests/find";
    private final String WIRE_MOCK_UNMATCHED_URL = "/__admin/requests/unmatched";
    private final String WIRE_MOCK_MATCHED_COUNT_URL = "/__admin/requests/count";

    private String getBaseUrl() {
        return "http://localhost:8282";
    }

    @SneakyThrows
    public Response getAllServerEvents(String wireMockBaseUrl) {
        return RestAssured.get(wireMockBaseUrl + WIRE_MOCK_URL);

    }

    @SneakyThrows
    private RequestSpecification buildFindRequest(@NonNull File request) {
        return RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(request)
                .given();
    }

    @SneakyThrows
    public String getServerEventBody(@NonNull File requestJson) {
        Response findServerEvents = buildFindRequest(requestJson).post(getBaseUrl() + WIRE_MOCK_FIND_URL);
        log.info("Found requests: \n" + findServerEvents);
        List<Map<String, String>> requests = findServerEvents.jsonPath().getList("requests");
        assertRequestCount(requests.size());
        return requests.get(0).get("body");
    }

    @SneakyThrows
    public Map<String, String> getServerEventQueryParams(@NonNull File requestJson) {
        Response findServerEvents = buildFindRequest(requestJson).post(getBaseUrl() + WIRE_MOCK_FIND_URL);
        log.info("Found requests: \n" + findServerEvents);
        assertRequestCount(findServerEvents.jsonPath().getList("requests").size());

        Map<String, Map<String, ArrayList<String>>> request = findServerEvents.jsonPath().getMap("requests[0].queryParams");
        Map<String, String> queryParams = new HashMap<>();
        Iterator<Map.Entry<String, Map<String, ArrayList<String>>>> iterator = request.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, ArrayList<String>>> pair = iterator.next();
            queryParams.put(pair.getKey(), pair.getValue().get("values").get(0));
        }
        return queryParams;
    }

    private void assertRequestCount(int size) {
        Assertions.assertTrue((size != 0), "No outer request was found");
        Assertions.assertTrue(size == 1, "More then one outer request was found");
    }

    @SneakyThrows
    public void deleteAllServerEvents() {
        RestAssured.delete(getBaseUrl() + WIRE_MOCK_URL);
    }

    @SneakyThrows
    public void checkUnmatchedRequest() {
        Response unmatchedServerEvents = RestAssured.get(getBaseUrl() + WIRE_MOCK_UNMATCHED_URL);
        List<Map<String, String>> requests = unmatchedServerEvents.jsonPath().getList("requests");
        log.info("Unmatched requests: \n" + requests);
        Assertions.assertTrue((requests.size() == 0), "Some unmatched request found");
    }

    @SneakyThrows
    public void checkMatchedRequest(int countExpected, File requestJson) {
        RequestSpecification matchedCountRequest = buildFindRequest(requestJson);
        log.info("Checking matched requests for: \n" + new String(Files.readAllBytes(Path.of(requestJson.getPath()))));
        Response matchedCountResponse = matchedCountRequest.post(getBaseUrl() + WIRE_MOCK_MATCHED_COUNT_URL);
        int countActual = Integer.parseInt(matchedCountResponse.jsonPath().getString("count"));
        Assertions.assertEquals(countExpected, countActual);
    }
}