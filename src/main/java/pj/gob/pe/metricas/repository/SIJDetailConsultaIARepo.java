package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;
import pj.gob.pe.metricas.repository.custom.CabConsultaIACustomRepo;
import pj.gob.pe.metricas.repository.custom.SIJDetailConsultaIACustomRepo;

public interface SIJDetailConsultaIARepo extends GenericRepo<SIJDetailConsultaIA, Long>, SIJDetailConsultaIACustomRepo {
}
