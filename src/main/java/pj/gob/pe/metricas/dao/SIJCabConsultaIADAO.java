package pj.gob.pe.metricas.dao;

import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;

import java.util.List;
import java.util.Map;

public interface SIJCabConsultaIADAO extends GenericDAO<SIJCabConsultaIA, Long>{

    List<SIJCabConsultaIA> getGeneralConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha);
}
