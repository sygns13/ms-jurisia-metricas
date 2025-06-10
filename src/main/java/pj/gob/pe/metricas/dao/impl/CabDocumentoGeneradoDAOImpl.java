package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.CabDocumentoGeneradoDAO;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.repository.CabDocumentoGeneradoRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class CabDocumentoGeneradoDAOImpl extends GenericDAOImpl<CabDocumentoGenerado, Long> implements CabDocumentoGeneradoDAO {

    private final CabDocumentoGeneradoRepo repo;

    @Override
    protected GenericRepo<CabDocumentoGenerado, Long> getRepo() {
        return repo;
    }
}
