package br.com.rafael.aiassistant.ai;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiProviderResponse;

public interface AiProvider {

    AiProviderResponse analyzeError(AiAnalysisRequest request);

    String getProviderName();


}