package pj.gob.pe.metricas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;

import java.util.Map;

public interface DemandasSentenciasToKafkaDAO extends GenericDAO<DemandasSentenciasToKafka, Long> {

    Page<DemandasSentenciasToKafka> getGeneralDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    Long getTotalDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters);
}
