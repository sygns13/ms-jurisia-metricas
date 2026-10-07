package pj.gob.pe.metricas.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pj.gob.pe.metricas.model.beans.DocumentoPlantillaGeneradoToKafka;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumer del tópico judicial-documentos-generado-v2 (documentos generados por plantilla en
 * ms-jurisia-judicial). Clase separada de {@link KafkaConsumerConfig}, igual que
 * {@link GeminiChatKafkaConsumerConfig}, para no modificar los consumers existentes.
 */
@Configuration
public class DocumentoPlantillaKafkaConsumerConfig {

    private final String BOOTSTRAP = "localhost:9094,localhost:9095,localhost:9096";

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;

    private DefaultJackson2JavaTypeMapper documentoPlantillaTypeMapper() {
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        // FQCN del header __TypeId__ (clase del productor en ms-jurisia-judicial) -> clase local
        Map<String, Class<?>> idToClazz = new HashMap<>();
        idToClazz.put(
                "pj.gob.pe.judicial.model.beans.DocumentoPlantillaGeneradoToKafka",
                DocumentoPlantillaGeneradoToKafka.class
        );
        mapper.setIdClassMapping(idToClazz);

        mapper.addTrustedPackages("pj.gob.pe.metricas.model.beans", "pj.gob.pe.judicial.model.beans");
        return mapper;
    }

    @Bean
    public ConsumerFactory<String, DocumentoPlantillaGeneradoToKafka> documentoPlantillaConsumerFactory() {
        JsonDeserializer<DocumentoPlantillaGeneradoToKafka> deserializer = new JsonDeserializer<>(DocumentoPlantillaGeneradoToKafka.class);
        deserializer.setTypeMapper(documentoPlantillaTypeMapper());
        deserializer.addTrustedPackages("pj.gob.pe.metricas.model.beans");

        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer
        );

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DocumentoPlantillaGeneradoToKafka> documentoPlantillaKafkaListenerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, DocumentoPlantillaGeneradoToKafka>();
        factory.setConsumerFactory(documentoPlantillaConsumerFactory());
        return factory;
    }
}
