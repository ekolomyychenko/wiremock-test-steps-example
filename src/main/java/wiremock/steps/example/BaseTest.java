package wiremock.steps.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Log4j2
public class BaseTest {

    @BeforeAll
    public static void before() {
        WIRE_MOCK_SERVER.start();
    }

    public static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(
            options()
                    .dynamicPort()
                    .withRootDirectory("src/test/resources/wiremock")
                    .extensions(new CustomResponseTemplateTransformer(), new ResponseTemplateTransformer(true))
                    .notifier(new ConsoleNotifier(true)));

    protected static String getBaseUrl() {
        return "http://localhost:" + WIRE_MOCK_SERVER.port();
    }
}