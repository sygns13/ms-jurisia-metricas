package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;
import pj.gob.pe.metricas.repository.custom.CabConsultaIACustomRepo;
import pj.gob.pe.metricas.repository.custom.SIJCabConsultaIACustomRepo;

public interface SIJCabConsultaIARepo extends GenericRepo<SIJCabConsultaIA, Long>, SIJCabConsultaIACustomRepo {
}
