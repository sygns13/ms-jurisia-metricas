package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;

import java.util.Map;

public interface CabDocumentoGeneradoCustomRepo {

    Page<CabDocumentoGenerado> getGeneralDocumentoGeneradoIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);
}
