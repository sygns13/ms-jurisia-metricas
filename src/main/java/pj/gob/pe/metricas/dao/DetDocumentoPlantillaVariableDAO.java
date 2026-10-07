package pj.gob.pe.metricas.dao;

import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;

import java.util.List;

public interface DetDocumentoPlantillaVariableDAO extends GenericDAO<DetDocumentoPlantillaVariable, Long> {

    List<DetDocumentoPlantillaVariable> listarPorCabecera(Long idCab);

    List<DetDocumentoPlantillaVariable> registrarTodos(List<DetDocumentoPlantillaVariable> variables);
}
