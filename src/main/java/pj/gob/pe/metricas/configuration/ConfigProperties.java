package pj.gob.pe.metricas.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ConfigProperties {


    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;

    @Value("${api.security.url}")
    private String urlSecurityAPI;

    @Value("${api.security.get.session.path}")
    private String pathGetSession;

    @Value("${api.security.get.users.path}")
    private String pathGetUsers;

    @Value("${spring.data.redis.prefix:jurisia_metrics}")
    private String REDIS_KEY_PREFIX;

    @Value("${spring.data.redis.ttl:3600}")
    private Long REDIS_TTL;


}
