package br.com.rafael.aiassistant.controller;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisAsyncResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisDetailResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisHistoryResponse;
import br.com.rafael.aiassistant.messaging.producer.ErrorAnalysisProducer;
import br.com.rafael.aiassistant.service.ErrorAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/error-analyses")
public class ErrorAnalysisController {

    private final ErrorAnalysisService service;
    private final ErrorAnalysisProducer producer;

    public ErrorAnalysisController(
            ErrorAnalysisService service,
            ErrorAnalysisProducer producer
    ) {
        this.service = service;
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<AiAnalysisResponse> analyze(@Valid @RequestBody AiAnalysisRequest request) {
        return ResponseEntity.ok(service.analyze(request));
    }

    @PostMapping("/async")
    public ResponseEntity<ErrorAnalysisAsyncResponse> analyzeAsync(
            @Valid @RequestBody AiAnalysisRequest request
    ) {
        ErrorAnalysisAsyncResponse response = service.createPendingAnalysis(request);

        producer.send(response.id());

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping
    public ResponseEntity<List<ErrorAnalysisHistoryResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ErrorAnalysisDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}