package br.com.rafael.aiassistant.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public ErrorAnalysis() {
    }

    public ErrorAnalysis(
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
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public String getProbableCause() {
        return probableCause;
    }

    public String getWhereToLook() {
        return whereToLook;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public String getRisk() {
        return risk;
    }

    public String getCategory() {
        return category;
    }

    public String getProvider() {
        return provider;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}