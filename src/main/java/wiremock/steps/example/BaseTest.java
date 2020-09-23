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
        wm.start();
    }

    public static final WireMockServer wm = new WireMockServer(
            options()
                    .port(8282)
                    .withRootDirectory("src/test/resources/wiremock")
                    .extensions(new CustomResponseTemplateTransformer()
                            , new ResponseTemplateTransformer(true)
                    )
                    .notifier(new ConsoleNotifier(true)));
}
