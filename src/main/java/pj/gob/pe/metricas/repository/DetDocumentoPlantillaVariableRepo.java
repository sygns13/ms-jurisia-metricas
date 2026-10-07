package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;

import java.util.List;

public interface DetDocumentoPlantillaVariableRepo extends GenericRepo<DetDocumentoPlantillaVariable, Long> {

    List<DetDocumentoPlantillaVariable> findByIdCabOrderByIdAsc(Long idCab);
}
