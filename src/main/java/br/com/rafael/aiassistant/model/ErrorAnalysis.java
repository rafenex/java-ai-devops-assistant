package br.com.rafael.aiassistant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "error_analysis")
public class ErrorAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String stacktrace;

    @Column(name = "probable_cause", columnDefinition = "TEXT")
    private String probableCause;

    @Column(name = "where_to_look", columnDefinition = "TEXT")
    private String whereToLook;

    @Column(name = "suggested_fix", columnDefinition = "TEXT")
    private String suggestedFix;

    @Column(columnDefinition = "TEXT")
    private String risk;

    private String category;

    private String provider;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ErrorAnalysisStatus status;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public ErrorAnalysis(
            @NotBlank(message = "O título é obrigatório")
            @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
            String title,

            @Size(max = 5000, message = "O contexto deve ter no máximo 5000 caracteres")
            String context,

            @NotBlank(message = "O stacktrace é obrigatório")
            @Size(max = 20000, message = "O stacktrace deve ter no máximo 20000 caracteres")
            String stacktrace,

            String probableCause,
            String whereToLook,
            String suggestedFix,
            String risk,
            String category,
            String provider,
            LocalDateTime createdAt
    ) {
        this.title = title;
        this.context = context;
        this.stacktrace = stacktrace;
        this.probableCause = probableCause;
        this.whereToLook = whereToLook;
        this.suggestedFix = suggestedFix;
        this.risk = risk;
        this.category = category;
        this.provider = provider;
        this.createdAt = createdAt;

        if (probableCause == null && whereToLook == null && suggestedFix == null) {
            this.status = ErrorAnalysisStatus.PENDING;
        } else {
            this.status = ErrorAnalysisStatus.DONE;
            this.processedAt = createdAt;
        }
    }

    public void markAsProcessing() {
        this.status = ErrorAnalysisStatus.PROCESSING;
    }

    public void markAsDone(
            String probableCause,
            String whereToLook,
            String suggestedFix,
            String risk,
            String category,
            String provider
    ) {
        this.probableCause = probableCause;
        this.whereToLook = whereToLook;
        this.suggestedFix = suggestedFix;
        this.risk = risk;
        this.category = category;
        this.provider = provider;
        this.status = ErrorAnalysisStatus.DONE;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = null;
    }

    public void markAsError(String errorMessage) {
        this.status = ErrorAnalysisStatus.ERROR;
        this.errorMessage = errorMessage;
        this.processedAt = LocalDateTime.now();
    }
}