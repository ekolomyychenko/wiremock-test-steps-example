# wiremock-test-steps-example

Examples demonstrating how to use WireMock as a mock server in integration tests with JUnit 5 and REST Assured.

## Features

- **Simple stub** — stub with no response body, just a status code
- **Stub with response body** — stub returning a JSON file as the response body
- **Scenario-based stubs** — different responses for the same URL depending on request order (using WireMock Scenarios)
- **Response templating** — dynamic response body using a custom `ResponseDefinitionTransformer` to inject runtime values (e.g. session/context variables)
- **WireMock Admin Client** — helper class to interact with the WireMock `/__admin` API for verifying received requests

## Tech stack

- Java 11
- JUnit 5
- WireMock 2.35.2 (standalone, embedded)
- REST Assured 5.4.0
- Allure 2.29.0
- Lombok
- Log4j2

## Project structure

```
src/
  main/java/wiremock/steps/example/
    BaseTest.java                        # Starts embedded WireMock server on a random free port
    WireMockClient.java                  # Admin API client for request verification
    CustomResponseTemplateTransformer.java  # Custom transformer for dynamic response values

  test/java/wiremock/steps/example/
    SimpleWireMockTest.java                        # Simple stub examples
    DifferentResponseForSecondRequestWireMockTest.java  # Scenario-based stub example
    ResponseTemplatingWireMockTest.java            # Custom response templating example

  test/resources/wiremock/
    mappings/          # Stub mapping JSON files
    __files/           # Response body JSON files
```

## Running the tests

```bash
mvn clean test
```

Сгенерировать и открыть Allure-отчёт:

```bash
mvn allure:serve
```

The embedded WireMock server starts automatically on a random free port before each test class via `@BeforeAll`.

## How it works

### Embedded WireMock server

`BaseTest` starts a `WireMockServer` on a random free port (`dynamicPort()`) with:
- Stub mappings loaded from `src/test/resources/wiremock/mappings/`
- Response bodies loaded from `src/test/resources/wiremock/__files/`
- `ResponseTemplateTransformer` (built-in) and `CustomResponseTemplateTransformer` (custom) registered as extensions

### Scenario-based stubs

The `DifferentResponseForSecondRequestWireMockTest` test sends two POST requests to the same URL and expects different response bodies. This is achieved using WireMock's [Stateful Scenarios](https://wiremock.org/docs/stateful-behaviour/) — the first mapping transitions state from `Started` to `Second successful request`, and the second mapping only matches in that state.

Mapping files: `mappings/first_request.json`, `mappings/second_request.json`

### Custom response transformer

`CustomResponseTemplateTransformer` replaces `{{placeholder}}` patterns in response bodies at runtime. This is useful for injecting values that are only known during test execution (e.g. generated IDs, session tokens).

To customize the replacement logic, edit the `transformResponse` method in `CustomResponseTemplateTransformer.java`.

Mapping file: `mappings/response_templating.json`

### WireMock Admin Client

`WireMockClient` wraps the WireMock `/__admin` API and provides:
- `getServerEventBody(File)` — get the request body of a matched request
- `getServerEventQueryParams(File)` — get query params of a matched request
- `checkMatchedRequest(int, File)` — assert a stub was called N times
- `checkUnmatchedRequest()` — assert no unmatched requests occurred
- `deleteAllServerEvents()` — clear recorded requests between tests
