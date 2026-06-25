package br.com.rafael.aiassistant.messaging.producer;

import br.com.rafael.aiassistant.config.RabbitMQConfig;
import br.com.rafael.aiassistant.dto.AiAnalysisRequest;
import br.com.rafael.aiassistant.messaging.dto.ErrorAnalysisMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ErrorAnalysisProducer {

    private final RabbitTemplate rabbitTemplate;

    public ErrorAnalysisProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Long analysisId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.AI_REQUEST_QUEUE,
                new ErrorAnalysisMessage(analysisId)
        );
    }
}