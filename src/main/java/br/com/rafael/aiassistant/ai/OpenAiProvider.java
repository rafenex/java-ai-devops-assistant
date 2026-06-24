package br.com.rafael.aiassistant.ai;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiProviderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;


    public OpenAiProvider(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public AiProviderResponse analyzeError(AiAnalysisRequest request) {
        String content = chatClient.prompt()
                .system("""
                        Você é um especialista Java/Spring Boot.
                        Analise erros técnicos de forma objetiva, prática e segura.
                        
                        Responda APENAS em JSON válido.
                        Não use markdown.
                        Não use ```json.
                        Não escreva texto fora do JSON.
                        """)
                .user("""
                        Analise o erro abaixo.
                        
                        Título:
                        %s
                        
                        Contexto:
                        %s
                        
                        Stacktrace:
                        %s
                        
                        Retorne exatamente este JSON:
                        
                        {
                          "probableCause": "causa provável do erro",
                          "whereToLook": "onde o desenvolvedor deve procurar",
                          "suggestedFix": "correção sugerida",
                          "risk": "risco ou impacto do problema",
                          "category": "categoria do erro"
                        }
                        """.formatted(
                        request.title(),
                        request.context(),
                        request.stacktrace()
                ))
                .call()
                .content();

        return parseResponse(content);
    }

    private AiProviderResponse parseResponse(String content) {
        try {
            return objectMapper.readValue(content, AiProviderResponse.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Erro ao converter resposta da IA para JSON", exception);
        }
    }

}