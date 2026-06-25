package br.com.rafael.aiassistant.messaging.producer;

import br.com.rafael.aiassistant.config.RabbitMQConfig;
import br.com.rafael.aiassistant.messaging.dto.AiRequestMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AiRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public AiRequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(AiRequestMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.AI_REQUEST_QUEUE,
                message
        );
    }
}