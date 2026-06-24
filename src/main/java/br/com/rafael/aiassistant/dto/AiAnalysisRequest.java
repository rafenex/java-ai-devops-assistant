package br.com.rafael.aiassistant.dto;

public record AiAnalysisRequest(
        String title,
        String context,
        String stacktrace
) {
}