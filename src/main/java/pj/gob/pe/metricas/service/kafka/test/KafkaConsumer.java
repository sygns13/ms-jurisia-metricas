package pj.gob.pe.metricas.service.kafka.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.model.beans.Completions;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

//    // Método para recibir mensajes del tópico "judicial-metrics"
//    @KafkaListener(
//            topics = "judicial-metrics",
//            groupId = "${spring.kafka.consumer.group-id}"
//    )
//    public void receiveMessage(String message) {
//        System.out.println("Mensaje recibido: " + message);
//        // Aquí tu lógica de procesamiento
//    }

//    @KafkaListener(
//            topics = "judicial-metrics",
//            groupId = "${spring.kafka.consumer.group-id}",
//            containerFactory = "completionsKafkaListenerFactory"
//    )
//    public void receiveMessage(
//            @Payload Completions  message,
//            @Header(KafkaHeaders.RECEIVED_KEY) String key,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
//    ) throws IOException {
//        //Completions completions = objectMapper.readValue(message, Completions.class);
//        System.out.println(String.format(
//                "Mensaje [%s] recibido con key [%s] de la partición %d @ %d",
//                message.toString(), key, partition, timestamp
//        ));
//    }
}
