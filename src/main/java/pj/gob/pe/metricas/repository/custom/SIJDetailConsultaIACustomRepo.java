package pj.gob.pe.metricas.repository.custom;

import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;

import java.util.List;
import java.util.Map;

public interface SIJDetailConsultaIACustomRepo {

    List<SIJDetailConsultaIA> getDetailConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha);
}
