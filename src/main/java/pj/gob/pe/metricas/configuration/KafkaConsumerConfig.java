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
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final String BOOTSTRAP = "localhost:9094,localhost:9095,localhost:9096";

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;

    private DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        // Mapea los FQCN que vienen en el header __TypeId__ a tus clases locales
        Map<String, Class<?>> idToClazz = new HashMap<>();
        idToClazz.put(
                "pj.gob.pe.consultaia.model.entities.Completions",
                pj.gob.pe.metricas.model.beans.Completions.class
        );

        idToClazz.put(
                "pj.gob.pe.judicial.model.beans.CabDocumentoGenerado",
                pj.gob.pe.metricas.model.entities.CabDocumentoGenerado.class
        );
        mapper.setIdClassMapping(idToClazz);

        // confía en cualquier paquete o en los específicos
        mapper.addTrustedPackages("pj.gob.pe.metricas.model.beans");
        return mapper;
    }

    //----------------------------------------
    // ConsumerFactory + ListenerFactory para Completions
    @Bean
    public ConsumerFactory<String, Completions> completionsConsumerFactory() {
        JsonDeserializer<Completions> deserializer = new JsonDeserializer<>(Completions.class);
        deserializer.setTypeMapper(typeMapper());
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
    public ConcurrentKafkaListenerContainerFactory<String, Completions> completionsKafkaListenerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Completions>();
        factory.setConsumerFactory(completionsConsumerFactory());
        return factory;
    }

    //----------------------------------------
    // ConsumerFactory + ListenerFactory para AuditEvent

    @Bean
    public ConsumerFactory<String, CabDocumentoGenerado> documentoGeneradoFactory() {
        JsonDeserializer<CabDocumentoGenerado> deserializer = new JsonDeserializer<>(CabDocumentoGenerado.class);
        deserializer.setTypeMapper(typeMapper());
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
    public ConcurrentKafkaListenerContainerFactory<String, CabDocumentoGenerado> auditKafkaListenerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, CabDocumentoGenerado>();
        factory.setConsumerFactory(documentoGeneradoFactory());
        return factory;
    }

}
