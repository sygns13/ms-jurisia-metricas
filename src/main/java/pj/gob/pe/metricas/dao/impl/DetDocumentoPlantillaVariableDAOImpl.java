package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DetDocumentoPlantillaVariableDAO;
import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;
import pj.gob.pe.metricas.repository.DetDocumentoPlantillaVariableRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DetDocumentoPlantillaVariableDAOImpl extends GenericDAOImpl<DetDocumentoPlantillaVariable, Long> implements DetDocumentoPlantillaVariableDAO {

    private final DetDocumentoPlantillaVariableRepo repo;

    @Override
    protected GenericRepo<DetDocumentoPlantillaVariable, Long> getRepo() {
        return repo;
    }

    @Override
    public List<DetDocumentoPlantillaVariable> listarPorCabecera(Long idCab) {
        return repo.findByIdCabOrderByIdAsc(idCab);
    }

    @Override
    public List<DetDocumentoPlantillaVariable> registrarTodos(List<DetDocumentoPlantillaVariable> variables) {
        return repo.saveAll(variables);
    }
}
