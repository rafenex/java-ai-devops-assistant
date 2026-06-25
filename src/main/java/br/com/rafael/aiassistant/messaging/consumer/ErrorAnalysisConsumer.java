package br.com.rafael.aiassistant.messaging.consumer;

import br.com.rafael.aiassistant.config.RabbitMQConfig;
import br.com.rafael.aiassistant.messaging.dto.ErrorAnalysisMessage;
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
    public void consume(ErrorAnalysisMessage message) {
        log.info("Mensagem recebida da fila. analysisId={}", message.analysisId());

        errorAnalysisService.processPendingAnalysis(message.analysisId());

        log.info("Análise assíncrona processada com sucesso. analysisId={}", message.analysisId());
    }
}