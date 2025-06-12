package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.repository.custom.CabDocumentoGeneradoCustomRepo;

public interface CabDocumentoGeneradoRepo extends GenericRepo<CabDocumentoGenerado, Long>, CabDocumentoGeneradoCustomRepo {
}
