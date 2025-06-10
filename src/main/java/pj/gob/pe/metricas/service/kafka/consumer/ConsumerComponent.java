package pj.gob.pe.metricas.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.service.business.ConsultaIAService;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoService;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ConsumerComponent {

    private final ConsultaIAService consultaIAService;
    private final DocumentoGeneradoService documentoGeneradoService;

    @KafkaListener(
            topics = "judicial-metrics",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "completionsKafkaListenerFactory"
    )
    public void receiveMessage(
            @Payload Completions message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
    ) throws Exception {
        System.out.println(String.format(
                "Mensaje [%s] recibido con key [%s] de la partición %d @ %d",
                message.toString(), key, partition, timestamp
        ));
        this.consultaIAService.RegistrarConsultaIA(message);
        //Completions completions = objectMapper.readValue(message, Completions.class);
//        System.out.println(String.format(
//                "Mensaje [%s] recibido con key [%s] de la partición %d @ %d",
//                message.toString(), key, partition, timestamp
//        ));
    }


    @KafkaListener(
            topics = "judicial-documentos-generado",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "completionsKafkaListenerFactory"
    )
    public void receiveDocumentoGenerado(
            @Payload CabDocumentoGenerado message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
    ) throws Exception {
        System.out.println(String.format(
                "Mensaje [%s] recibido con key [%s] de la partición %d @ %d",
                message.toString(), key, partition, timestamp
        ));
        this.documentoGeneradoService.RegistrarDocumentoGenerado(message);
        //Completions completions = objectMapper.readValue(message, Completions.class);
//        System.out.println(String.format(
//                "Mensaje [%s] recibido con key [%s] de la partición %d @ %d",
//                message.toString(), key, partition, timestamp
//        ));
    }
}
