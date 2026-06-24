package br.com.rafael.aiassistant.controller;

import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.service.ErrorAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}