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
import pj.gob.pe.metricas.model.beans.GeminiChatToKafka;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración del consumer del tópico judicial-metrics-gemini-chats (chat conversacional con
 * Gemini). Se define en clase separada de {@link KafkaConsumerConfig} para no modificar la
 * configuración existente de los consumers en producción; sigue exactamente el mismo patrón
 * (JsonDeserializer + typeMapper que traduce el FQCN del header __TypeId__ del productor a la
 * clase local).
 */
@Configuration
public class GeminiChatKafkaConsumerConfig {

    private final String BOOTSTRAP = "localhost:9094,localhost:9095,localhost:9096";

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;

    private DefaultJackson2JavaTypeMapper geminiChatTypeMapper() {
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        // Mapea el FQCN que viene en el header __TypeId__ (clase del productor en
        // ms-jurisia-consultaia) a la clase local de este microservicio.
        Map<String, Class<?>> idToClazz = new HashMap<>();
        idToClazz.put(
                "pj.gob.pe.consultaia.utils.beans.responses.ResponseGeminiChat",
                pj.gob.pe.metricas.model.beans.GeminiChatToKafka.class
        );
        mapper.setIdClassMapping(idToClazz);

        mapper.addTrustedPackages("pj.gob.pe.metricas.model.beans", "pj.gob.pe.consultaia.utils.beans.responses");
        return mapper;
    }

    @Bean
    public ConsumerFactory<String, GeminiChatToKafka> geminiChatConsumerFactory() {
        JsonDeserializer<GeminiChatToKafka> deserializer = new JsonDeserializer<>(GeminiChatToKafka.class);
        deserializer.setTypeMapper(geminiChatTypeMapper());
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
    public ConcurrentKafkaListenerContainerFactory<String, GeminiChatToKafka> geminiChatKafkaListenerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, GeminiChatToKafka>();
        factory.setConsumerFactory(geminiChatConsumerFactory());
        return factory;
    }
}
