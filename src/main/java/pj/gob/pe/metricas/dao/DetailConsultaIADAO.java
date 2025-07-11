package pj.gob.pe.metricas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;

import java.util.Map;

public interface DetailConsultaIADAO extends GenericDAO<DetailConsultaIA, Long>{

    Page<DetailConsultaIA> getDetailConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);
}
