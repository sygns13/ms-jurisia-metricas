package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;

import java.util.Map;

public interface DemandasSentenciasCustomRepo {

    Page<DemandasSentenciasToKafka> getGeneralDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    Long getTotalDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters);
}
