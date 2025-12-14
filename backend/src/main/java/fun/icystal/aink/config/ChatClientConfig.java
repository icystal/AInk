package fun.icystal.aink.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Bean
    public ChatClient stepChatClient(
            @Value("${aink.ai.qwen.step.model}") String model,
            @Value("${aink.ai.qwen.step.temperature}") Double temperature,
            @Value("${aink.ai.qwen.step.max-tokens}") Integer maxTokens,
            ObservationRegistry observationRegistry
    ) {
        DashScopeApi api = DashScopeApi.builder().apiKey(dashScopeApiKey).build();
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature)
                .withMaxToken(maxTokens)
                .build();
        DashScopeChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(api)
                .defaultOptions(options)
                .observationRegistry(observationRegistry)
                .build();
        return ChatClient
                .builder(chatModel, observationRegistry, null)
                .build();
    }

}
