package pj.gob.pe.metricas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;

import java.util.Map;

public interface CabConsultaIADAO extends GenericDAO<CabConsultaIA, Long> {

    CabConsultaIA findBySessionUID(String sessionUID);

    Page<CabConsultaIA> getGeneralConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);
}
