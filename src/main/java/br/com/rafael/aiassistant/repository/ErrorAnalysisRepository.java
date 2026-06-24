package br.com.rafael.aiassistant.repository;

import br.com.rafael.aiassistant.model.ErrorAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorAnalysisRepository extends JpaRepository<ErrorAnalysis, Long> {
}