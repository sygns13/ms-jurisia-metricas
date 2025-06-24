package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;

import java.util.List;
import java.util.Map;

public interface SIJCabConsultaIACustomRepo {

    List<SIJCabConsultaIA> getGeneralConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha);
}
