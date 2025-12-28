package fun.icystal.aink.fundamental;

import fun.icystal.aink.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.util.LoggingMarkers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;

import java.util.List;

@Slf4j
public class CustomListOutputConverter<T> implements StructuredOutputConverter<List<T>>  {

    private final CustomBeanOutputConverter<T> customBeanOutputConverter;

    private final Class<T> clazz;

    public CustomListOutputConverter(Class<T> clazz) {
        this.clazz = clazz;
        customBeanOutputConverter = new CustomBeanOutputConverter<T>(ParameterizedTypeReference.forType(clazz));
    }

    @Override
    public String getFormat() {
        String template = """
                Your response should be a valid JSON array, starting with [ and ending with ].
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Do not include markdown code blocks in your response.
                Remove the ```json markdown from the output.
                Here is the JSON Schema instance that each element in the JSON array your output must adhere to:
                ```%s```
                """;
        return String.format(template, customBeanOutputConverter.jsonSchema);
    }

    @Override
    public List<T> convert(@NonNull String text) {
        try {
            text = text.trim();
            if (text.startsWith("```") && text.endsWith("```")) {
                String[] lines = text.split("\n", 2);
                if (lines[0].trim().equalsIgnoreCase("```json")) {
                    text = lines.length > 1 ? lines[1] : "";
                } else {
                    text = text.substring(3);
                }

                text = text.substring(0, text.length() - 3);
                text = text.trim();
            }

            return JsonUtil.parseArray(text, clazz);
        } catch (Exception e) {
            log.error(LoggingMarkers.SENSITIVE_DATA_MARKER, "Could not parse the given text to the desired target type: \"{}\" into {}", text, clazz.getName());
            throw new RuntimeException(e);
        }
    }
}
