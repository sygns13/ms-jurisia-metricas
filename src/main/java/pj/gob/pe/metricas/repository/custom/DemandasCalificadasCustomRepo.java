package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;

import java.util.Map;

public interface DemandasCalificadasCustomRepo {

    Page<DemandasCalificadasToKafka> getGeneralDemandasCalificadas(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    Long getTotalDemandasCalificadas(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters);
}
