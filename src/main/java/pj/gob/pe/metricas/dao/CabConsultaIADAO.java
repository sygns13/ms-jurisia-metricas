package pj.gob.pe.metricas.dao;

import org.springframework.data.repository.query.Param;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;

public interface CabConsultaIADAO extends GenericDAO<CabConsultaIA, Long> {

    CabConsultaIA findBySessionUID(String sessionUID);
}
