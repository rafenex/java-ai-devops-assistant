package br.com.rafael.aiassistant.controller;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisDetailResponse;
import br.com.rafael.aiassistant.dto.ErrorAnalysisHistoryResponse;
import br.com.rafael.aiassistant.service.ErrorAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/error-analyses")
public class ErrorAnalysisController {

    private final ErrorAnalysisService service;

    public ErrorAnalysisController(ErrorAnalysisService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AiAnalysisResponse> analyze(@RequestBody AiAnalysisRequest request) {
        return ResponseEntity.ok(service.analyze(request));
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