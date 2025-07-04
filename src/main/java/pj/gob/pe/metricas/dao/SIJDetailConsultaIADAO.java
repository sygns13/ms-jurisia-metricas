package pj.gob.pe.metricas.dao;

import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;

import java.util.List;
import java.util.Map;

public interface SIJDetailConsultaIADAO extends GenericDAO<SIJDetailConsultaIA, Long>{

    List<SIJDetailConsultaIA> getDetailConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha);
}
