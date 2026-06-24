package br.com.rafael.aiassistant.dto;

import java.time.LocalDateTime;

public record ErrorAnalysisDetailResponse(
        Long id,
        String title,
        String context,
        String stacktrace,
        String probableCause,
        String whereToLook,
        String suggestedFix,
        String risk,
        String category,
        String provider,
        LocalDateTime createdAt
) {
}