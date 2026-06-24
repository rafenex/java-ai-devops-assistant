package br.com.rafael.aiassistant.service;

import br.com.rafael.aiassistant.ai.AiProvider;
import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public class ErrorAnalysisService {

    private final AiProvider aiProvider;

    public ErrorAnalysisService(AiProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    public AiAnalysisResponse analyze(AiAnalysisRequest request) {
        return aiProvider.analyzeError(request);
    }
}