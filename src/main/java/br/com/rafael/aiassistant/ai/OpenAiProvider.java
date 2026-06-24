package br.com.rafael.aiassistant.ai;
import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.ai.provider",
        havingValue = "openai"
)
public class OpenAiProvider implements AiProvider {

    private final ChatClient chatClient;

    public OpenAiProvider(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public AiAnalysisResponse analyzeError(AiAnalysisRequest request) {
        String content = chatClient.prompt()
                .system("""
                        Você é um especialista Java/Spring Boot.
                        Analise erros técnicos de forma objetiva, prática e segura.
                        Responda sempre no formato solicitado.
                        """)
                .user("""
                        Título:
                        %s

                        Contexto:
                        %s

                        Stacktrace:
                        %s

                        Responda exatamente neste formato:

                        Causa provável:
                        ...

                        Onde procurar:
                        ...

                        Correção sugerida:
                        ...

                        Risco:
                        ...

                        Categoria:
                        ...
                        """.formatted(
                        request.title(),
                        request.context(),
                        request.stacktrace()
                ))
                .call()
                .content();

        return parseResponse(content);
    }

    private AiAnalysisResponse parseResponse(String content) {
        return new AiAnalysisResponse(
                extract(content, "Causa provável:", "Onde procurar:"),
                extract(content, "Onde procurar:", "Correção sugerida:"),
                extract(content, "Correção sugerida:", "Risco:"),
                extract(content, "Risco:", "Categoria:"),
                extract(content, "Categoria:", null)
        );
    }

    private String extract(String text, String start, String end) {
        int startIndex = text.indexOf(start);

        if (startIndex == -1) {
            return "";
        }

        startIndex += start.length();

        int endIndex = end == null ? text.length() : text.indexOf(end, startIndex);

        if (endIndex == -1) {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex).trim();
    }
}