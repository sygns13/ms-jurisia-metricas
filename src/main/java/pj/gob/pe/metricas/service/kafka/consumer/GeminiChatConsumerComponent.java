package pj.gob.pe.metricas.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.model.beans.GeminiChatToKafka;
import pj.gob.pe.metricas.service.business.GeminiChatMetricsService;

/**
 * Consumer del tópico judicial-metrics-gemini-chats (turnos del chat conversacional con Gemini,
 * publicados por ms-jurisia-consultaia). Componente separado de {@link ConsumerComponent} para no
 * modificar los consumers existentes; mismo patrón de listener.
 */
@Component
@RequiredArgsConstructor
public class GeminiChatConsumerComponent {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatConsumerComponent.class);

    private final GeminiChatMetricsService geminiChatMetricsService;

    @KafkaListener(
            topics = "judicial-metrics-gemini-chats",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "geminiChatKafkaListenerFactory"
    )
    public void receiveGeminiChat(
            @Payload GeminiChatToKafka message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
    ) throws Exception {
        logger.info("Mensaje GeminiChat [id={}, sessionUID={}] recibido con key [{}] de la partición {} @ {}",
                message.getId(), message.getSessionUID(), key, partition, timestamp);
        this.geminiChatMetricsService.RegistrarGeminiChat(message);
    }
}
