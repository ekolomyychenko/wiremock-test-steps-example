package wiremock.steps.example;

import io.restassured.response.Response;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Log4j2
public class WireMockAssertions {

    private final WireMockAdminClient client;

    public WireMockAssertions(WireMockAdminClient client) {
        this.client = client;
    }

    public String getRequestBody(@NonNull File requestJson) {
        Response response = client.findRequests(requestJson);
        log.info("Found requests: \n{}", response);
        List<Map<String, String>> requests = response.jsonPath().getList("requests");
        assertSingleRequest(requests.size());
        return requests.get(0).get("body");
    }

    public Map<String, String> getRequestQueryParams(@NonNull File requestJson) {
        Response response = client.findRequests(requestJson);
        log.info("Found requests: \n{}", response);
        assertSingleRequest(response.jsonPath().getList("requests").size());

        Map<String, Map<String, ArrayList<String>>> request = response.jsonPath().getMap("requests[0].queryParams");
        Map<String, String> queryParams = new HashMap<>();
        for (Map.Entry<String, Map<String, ArrayList<String>>> pair : request.entrySet()) {
            queryParams.put(pair.getKey(), pair.getValue().get("values").get(0));
        }
        return queryParams;
    }

    public void assertNoUnmatchedRequests() {
        List<Map<String, String>> requests = client.getUnmatchedRequests().jsonPath().getList("requests");
        log.info("Unmatched requests: \n{}", requests);
        Assertions.assertTrue(requests.isEmpty(), "Some unmatched request found");
    }

    @SneakyThrows
    public void assertMatchedRequestCount(int countExpected, @NonNull File requestJson) {
        log.info("Checking matched requests for: \n{}", new String(Files.readAllBytes(Path.of(requestJson.getPath()))));
        int countActual = Integer.parseInt(client.getMatchedCount(requestJson).jsonPath().getString("count"));
        Assertions.assertEquals(countExpected, countActual);
    }

    private void assertSingleRequest(int size) {
        Assertions.assertTrue(size != 0, "No outer request was found");
        Assertions.assertEquals(1, size, "More than one outer request was found");
    }
}
