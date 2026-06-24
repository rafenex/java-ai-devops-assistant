package br.com.rafael.aiassistant.service;

import br.com.rafael.aiassistant.ai.AiProvider;
import br.com.rafael.aiassistant.dto.*;
import br.com.rafael.aiassistant.exception.NotFoundException;
import br.com.rafael.aiassistant.model.ErrorAnalysis;
import br.com.rafael.aiassistant.repository.ErrorAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ErrorAnalysisService {

    private final AiProvider aiProvider;
    private final ErrorAnalysisRepository repository;

    public ErrorAnalysisService(
            AiProvider aiProvider,
            ErrorAnalysisRepository repository
    ) {
        this.aiProvider = aiProvider;
        this.repository = repository;
    }

    public AiAnalysisResponse analyze(AiAnalysisRequest request) {
        AiProviderResponse aiResponse = aiProvider.analyzeError(request);

        ErrorAnalysis savedAnalysis = repository.save(new ErrorAnalysis(
                request.title(),
                request.context(),
                request.stacktrace(),
                aiResponse.probableCause(),
                aiResponse.whereToLook(),
                aiResponse.suggestedFix(),
                aiResponse.risk(),
                aiResponse.category(),
                aiProvider.getProviderName(),
                LocalDateTime.now()
        ));

        return new AiAnalysisResponse(
                savedAnalysis.getId(),
                savedAnalysis.getProbableCause(),
                savedAnalysis.getWhereToLook(),
                savedAnalysis.getSuggestedFix(),
                savedAnalysis.getRisk(),
                savedAnalysis.getCategory()
        );
    }

    public List<ErrorAnalysisHistoryResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(analysis -> new ErrorAnalysisHistoryResponse(
                        analysis.getId(),
                        analysis.getTitle(),
                        analysis.getCategory(),
                        analysis.getProvider(),
                        analysis.getCreatedAt()
                ))
                .toList();
    }

    public ErrorAnalysisDetailResponse findById(Long id) {
        ErrorAnalysis analysis = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Análise não encontrada: " + id));

        return new ErrorAnalysisDetailResponse(
                analysis.getId(),
                analysis.getTitle(),
                analysis.getContext(),
                analysis.getStacktrace(),
                analysis.getProbableCause(),
                analysis.getWhereToLook(),
                analysis.getSuggestedFix(),
                analysis.getRisk(),
                analysis.getCategory(),
                analysis.getProvider(),
                analysis.getCreatedAt()
        );
    }
}