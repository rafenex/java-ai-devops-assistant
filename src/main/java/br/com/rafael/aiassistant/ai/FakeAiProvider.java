package br.com.rafael.aiassistant.ai;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiProviderResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "app.ai.provider",
        havingValue = "fake",
        matchIfMissing = true
)
@Component
public class FakeAiProvider implements AiProvider {

    @Override
    public AiProviderResponse analyzeError(AiAnalysisRequest request) {
        return new AiProviderResponse(
                "Provável erro de configuração ou dependência no contexto: " + request.context(),
                "Verifique controller, service, repository e application.properties.",
                "Analise o stacktrace e identifique o primeiro erro causado pela aplicação.",
                "Baixo, pois esta é uma resposta fake apenas para validar o fluxo.",
                "SPRING"
        );
    }


    @Override
    public String getProviderName() {
        return "fake";
    }

}