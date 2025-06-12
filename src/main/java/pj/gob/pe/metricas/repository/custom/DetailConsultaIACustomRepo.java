package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;

import java.util.Map;

public interface DetailConsultaIACustomRepo {

    Page<DetailConsultaIA> getDetailConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);
}
