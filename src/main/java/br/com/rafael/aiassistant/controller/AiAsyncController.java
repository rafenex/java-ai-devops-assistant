package br.com.rafael.aiassistant.controller;

import br.com.rafael.aiassistant.messaging.dto.AiRequestMessage;
import br.com.rafael.aiassistant.messaging.producer.AiRequestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiAsyncController {

    private final AiRequestProducer aiRequestProducer;

    public AiAsyncController(AiRequestProducer aiRequestProducer) {
        this.aiRequestProducer = aiRequestProducer;
    }

    @PostMapping("/async")
    public ResponseEntity<Void> sendAsync(@RequestBody AiRequestMessage request) {
        aiRequestProducer.send(request);
        return ResponseEntity.accepted().build();
    }
}