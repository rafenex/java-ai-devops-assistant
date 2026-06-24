package br.com.rafael.aiassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiAnalysisRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String title,

        @Size(max = 5000, message = "O contexto deve ter no máximo 5000 caracteres")
        String context,

        @NotBlank(message = "O stacktrace é obrigatório")
        @Size(max = 20000, message = "O stacktrace deve ter no máximo 20000 caracteres")
        String stacktrace
) {
}