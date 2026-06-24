package br.com.rafael.aiassistant.dto;

public record AiProviderResponse(
        String probableCause,
        String whereToLook,
        String suggestedFix,
        String risk,
        String category
) {
}