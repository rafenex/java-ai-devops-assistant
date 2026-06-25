package br.com.rafael.aiassistant.messaging.consumer;

import br.com.rafael.aiassistant.config.RabbitMQConfig;
import br.com.rafael.aiassistant.messaging.dto.AiRequestMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AiRequestConsumer {

    @RabbitListener(queues = RabbitMQConfig.AI_REQUEST_QUEUE)
    public void consume(AiRequestMessage message) {
        System.out.println("Mensagem recebida da fila: " + message.question());
    }
}