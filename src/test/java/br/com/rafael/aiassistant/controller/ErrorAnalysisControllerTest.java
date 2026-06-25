package br.com.rafael.aiassistant.controller;

import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisAsyncResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisDetailResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisHistoryResponse;
import br.com.rafael.aiassistant.exception.NotFoundException;
import br.com.rafael.aiassistant.messaging.producer.ErrorAnalysisProducer;
import br.com.rafael.aiassistant.service.ErrorAnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ErrorAnalysisController.class)
class ErrorAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ErrorAnalysisService service;

    @MockitoBean
    private ErrorAnalysisProducer producer;

    @Test
    void analyze_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        AiAnalysisResponse response = new AiAnalysisResponse(
                1L,
                "Falha na criação de bean",
                "Verifique service e repository",
                "Corrija a dependência ausente",
                "Aplicação não inicia",
                "SPRING"
        );

        when(service.analyze(any())).thenReturn(response);

        String requestJson = """
                {
                  "title": "Erro ao subir aplicação",
                  "context": "Spring Boot com PostgreSQL",
                  "stacktrace": "UnsatisfiedDependencyException"
                }
                """;

        mockMvc.perform(post("/api/error-analyses")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.probableCause").value("Falha na criação de bean"))
                .andExpect(jsonPath("$.whereToLook").value("Verifique service e repository"))
                .andExpect(jsonPath("$.suggestedFix").value("Corrija a dependência ausente"))
                .andExpect(jsonPath("$.risk").value("Aplicação não inicia"))
                .andExpect(jsonPath("$.category").value("SPRING"));
    }

    @Test
    void analyzeAsync_ShouldReturnAccepted_WhenRequestIsValid() throws Exception {
        ErrorAnalysisAsyncResponse response = new ErrorAnalysisAsyncResponse(
                10L,
                "PENDING"
        );

        when(service.createPendingAnalysis(any())).thenReturn(response);

        String requestJson = """
                {
                  "title": "Erro ao subir aplicação",
                  "context": "Spring Boot com PostgreSQL",
                  "stacktrace": "UnsatisfiedDependencyException"
                }
                """;

        mockMvc.perform(post("/api/error-analyses/async")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(service).createPendingAnalysis(any());
        verify(producer).send(10L);
    }

    @Test
    void analyze_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        String requestJson = """
                {
                  "title": "",
                  "context": "teste",
                  "stacktrace": ""
                }
                """;

        mockMvc.perform(post("/api/error-analyses")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void analyzeAsync_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        String requestJson = """
                {
                  "title": "",
                  "context": "teste",
                  "stacktrace": ""
                }
                """;

        mockMvc.perform(post("/api/error-analyses/async")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void findAll_ShouldReturnHistoryList() throws Exception {
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 24, 10, 0);

        List<ErrorAnalysisHistoryResponse> response = List.of(
                new ErrorAnalysisHistoryResponse(
                        1L,
                        "Erro ao subir aplicação",
                        "SPRING",
                        "openai",
                        "DONE",
                        createdAt
                )
        );

        when(service.findAll()).thenReturn(response);

        mockMvc.perform(get("/api/error-analyses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Erro ao subir aplicação"))
                .andExpect(jsonPath("$[0].category").value("SPRING"))
                .andExpect(jsonPath("$[0].provider").value("openai"))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    void findById_ShouldReturnDetail_WhenAnalysisExists() throws Exception {
        ErrorAnalysisDetailResponse response = getErrorAnalysisDetailResponse();

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/error-analyses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Erro ao subir aplicação"))
                .andExpect(jsonPath("$.category").value("SPRING"))
                .andExpect(jsonPath("$.provider").value("openai"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.errorMessage").doesNotExist());
    }

    @Test
    void findById_ShouldReturnNotFound_WhenAnalysisDoesNotExist() throws Exception {
        when(service.findById(999L))
                .thenThrow(new NotFoundException("Análise não encontrada: 999"));

        mockMvc.perform(get("/api/error-analyses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Análise não encontrada: 999"));
    }

    private static ErrorAnalysisDetailResponse getErrorAnalysisDetailResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 24, 10, 0);
        LocalDateTime processedAt = LocalDateTime.of(2026, 6, 24, 10, 1);

        return new ErrorAnalysisDetailResponse(
                1L,
                "Erro ao subir aplicação",
                "Spring Boot com PostgreSQL",
                "UnsatisfiedDependencyException",
                "Falha na criação de bean",
                "Verifique service e repository",
                "Corrija a dependência ausente",
                "Aplicação não inicia",
                "SPRING",
                "openai",
                "DONE",
                createdAt,
                processedAt,
                null
        );
    }
}