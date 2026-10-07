package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.repository.custom.CabDocumentoPlantillaGeneradoCustomRepo;

import java.util.Optional;

public interface CabDocumentoPlantillaGeneradoRepo extends GenericRepo<CabDocumentoPlantillaGenerado, Long>, CabDocumentoPlantillaGeneradoCustomRepo {

    Optional<CabDocumentoPlantillaGenerado> findFirstBySessionUID(String sessionUID);
}
