package br.com.rafael.aiassistant.ai;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;

public interface AiProvider {

    AiAnalysisResponse analyzeError(AiAnalysisRequest request);
}