package pj.gob.pe.metricas.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.model.beans.DocumentoPlantillaGeneradoToKafka;
import pj.gob.pe.metricas.service.business.DocumentoPlantillaGeneradoService;

/**
 * Consumer del tópico judicial-documentos-generado-v2 (documentos generados por plantilla en
 * ms-jurisia-judicial). Componente separado de {@link ConsumerComponent} para no modificar los
 * consumers existentes.
 */
@Component
@RequiredArgsConstructor
public class DocumentoPlantillaConsumerComponent {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoPlantillaConsumerComponent.class);

    private final DocumentoPlantillaGeneradoService documentoPlantillaGeneradoService;

    @KafkaListener(
            topics = "judicial-documentos-generado-v2",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "documentoPlantillaKafkaListenerFactory"
    )
    public void receiveDocumentoPlantilla(
            @Payload DocumentoPlantillaGeneradoToKafka message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
    ) throws Exception {
        logger.info("Mensaje DocumentoPlantilla [sessionUID={}, status={}, idDocumento={}] recibido con key [{}] de la partición {} @ {}",
                message.getSessionUID(), message.getStatus(), message.getIdDocumento(), key, partition, timestamp);
        this.documentoPlantillaGeneradoService.RegistrarDocumentoPlantillaGenerado(message);
    }
}
