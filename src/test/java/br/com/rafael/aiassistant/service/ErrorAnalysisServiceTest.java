package br.com.rafael.aiassistant.service;

import br.com.rafael.aiassistant.ai.AiProvider;
import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.dto.AiProviderResponse;
import br.com.rafael.aiassistant.exception.NotFoundException;
import br.com.rafael.aiassistant.model.ErrorAnalysis;
import br.com.rafael.aiassistant.repository.ErrorAnalysisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorAnalysisServiceTest {

    @Mock
    private AiProvider aiProvider;

    @Mock
    private ErrorAnalysisRepository repository;

    @InjectMocks
    private ErrorAnalysisService service;

    @Test
    void analyze_ShouldCallAiProviderSaveAnalysisAndReturnResponse() {
        AiAnalysisRequest request = new AiAnalysisRequest(
                "Erro ao subir aplicação",
                "Spring Boot com PostgreSQL",
                "UnsatisfiedDependencyException"
        );

        AiProviderResponse aiResponse = new AiProviderResponse(
                "Falha na criação de bean",
                "Verifique service e repository",
                "Corrija a dependência ausente",
                "Aplicação não inicia",
                "SPRING"
        );

        ErrorAnalysis savedEntity = new ErrorAnalysis(
                request.title(),
                request.context(),
                request.stacktrace(),
                aiResponse.probableCause(),
                aiResponse.whereToLook(),
                aiResponse.suggestedFix(),
                aiResponse.risk(),
                aiResponse.category(),
                "fake",
                LocalDateTime.now()
        );

        // Como o id é gerado pelo banco e sua entidade não tem setter,
        // vamos usar o retorno sem validar id por enquanto.
        when(aiProvider.analyzeError(request)).thenReturn(aiResponse);
        when(aiProvider.getProviderName()).thenReturn("fake");
        when(repository.save(any(ErrorAnalysis.class))).thenReturn(savedEntity);

        AiAnalysisResponse response = service.analyze(request);

        assertNotNull(response);
        assertEquals("Falha na criação de bean", response.probableCause());
        assertEquals("Verifique service e repository", response.whereToLook());
        assertEquals("Corrija a dependência ausente", response.suggestedFix());
        assertEquals("Aplicação não inicia", response.risk());
        assertEquals("SPRING", response.category());

        verify(aiProvider).analyzeError(request);
        verify(aiProvider).getProviderName();
        verify(repository).save(any(ErrorAnalysis.class));
    }

    @Test
    void findById_ShouldReturnAnalysis_WhenAnalysisExists() {
        Long id = 1L;

        ErrorAnalysis analysis = new ErrorAnalysis(
                "Erro ao subir aplicação",
                "Spring Boot com PostgreSQL",
                "UnsatisfiedDependencyException",
                "Falha na criação de bean",
                "Verifique service e repository",
                "Corrija a dependência ausente",
                "Aplicação não inicia",
                "SPRING",
                "fake",
                LocalDateTime.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(analysis));

        var response = service.findById(id);

        assertNotNull(response);
        assertEquals("Erro ao subir aplicação", response.title());
        assertEquals("SPRING", response.category());
        assertEquals("fake", response.provider());

        verify(repository).findById(id);
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenAnalysisDoesNotExist() {
        Long id = 999L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.findById(id)
        );

        assertEquals("Análise não encontrada: 999", exception.getMessage());

        verify(repository).findById(id);
    }

    @Test
    void findAll_ShouldReturnAnalysisHistoryList() {
        ErrorAnalysis analysis = new ErrorAnalysis(
                "Erro ao subir aplicação",
                "Spring Boot com PostgreSQL",
                "UnsatisfiedDependencyException",
                "Falha na criação de bean",
                "Verifique service e repository",
                "Corrija a dependência ausente",
                "Aplicação não inicia",
                "SPRING",
                "fake",
                LocalDateTime.now()
        );

        when(repository.findAll()).thenReturn(List.of(analysis));

        var response = service.findAll();

        assertEquals(1, response.size());
        assertEquals("Erro ao subir aplicação", response.getFirst().title());
        assertEquals("SPRING", response.getFirst().category());
        assertEquals("fake", response.getFirst().provider());

        verify(repository).findAll();
    }
}