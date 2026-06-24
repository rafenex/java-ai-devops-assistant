package br.com.rafael.aiassistant.dto;

public record AiAnalysisResponse(
        String probableCause,
        String whereToLook,
        String suggestedFix,
        String risk,
        String category
) {
}