package wiremock.steps.example;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class CustomResponseTemplateTransformer extends ResponseDefinitionTransformer {

    private static final String TRANSFORMER_NAME = "custom-transformer";
    private static final boolean APPLY_GLOBALLY = false;
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{((?:(?!\\}\\}|\\{\\{).)+)\\}\\}");

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        String body;
        if (responseDefinition.getBody() != null) {
            body = responseDefinition.getBody();
        } else {
            BinaryFile binaryFile = fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName());
            body = new String(binaryFile.readContents(), StandardCharsets.UTF_8);
        }
        return ResponseDefinitionBuilder
                .like(responseDefinition).but()
                .withBodyFile(null)
                .withBody(transformResponse(body))
                .build();
    }

    private String transformResponse(String response) {
        String modifiedResponse = response;
        Matcher matcher = VARIABLE_PATTERN.matcher(modifiedResponse);
        StringBuffer buffer = null;
        while (matcher.find()) {
            if (buffer == null) {
                buffer = new StringBuffer();
            }

            // todo: here you can write your custom replacement.
            //  Put some session/context variables or other data that you don't know before test run
            matcher.appendReplacement(buffer, "lol kek");
        }
        if (buffer != null) {
            matcher.appendTail(buffer);
            modifiedResponse = buffer.toString();
        }
        return modifiedResponse;
    }

    @Override
    public String getName() {
        return TRANSFORMER_NAME;
    }

    @Override
    public boolean applyGlobally() {
        return APPLY_GLOBALLY;
    }
}
