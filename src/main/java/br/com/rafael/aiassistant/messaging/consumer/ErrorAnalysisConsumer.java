package br.com.rafael.aiassistant.messaging.consumer;

import br.com.rafael.aiassistant.config.RabbitMQConfig;
import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.dto.AiAnalysisResponse;
import br.com.rafael.aiassistant.service.ErrorAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ErrorAnalysisConsumer {

    private static final Logger log = LoggerFactory.getLogger(ErrorAnalysisConsumer.class);

    private final ErrorAnalysisService errorAnalysisService;

    public ErrorAnalysisConsumer(ErrorAnalysisService errorAnalysisService) {
        this.errorAnalysisService = errorAnalysisService;
    }

    @RabbitListener(queues = RabbitMQConfig.AI_REQUEST_QUEUE)
    public void consume(AiAnalysisRequest request) {
        log.info("Mensagem recebida da fila para análise. title={}", request.title());

        AiAnalysisResponse response = errorAnalysisService.analyze(request);

        log.info(
                "Análise processada com sucesso. id={}, category={}, risk={}",
                response.id(),
                response.category(),
                response.risk()
        );
    }
}