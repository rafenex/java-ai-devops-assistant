package br.com.rafael.aiassistant.dto;

import java.time.LocalDateTime;

public record ErrorAnalysisHistoryResponse(
        Long id,
        String title,
        String category,
        String provider,
        String status,
        LocalDateTime createdAt
) {
}