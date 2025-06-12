package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.CabDocumentoGeneradoDAO;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.repository.CabDocumentoGeneradoRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CabDocumentoGeneradoDAOImpl extends GenericDAOImpl<CabDocumentoGenerado, Long> implements CabDocumentoGeneradoDAO {

    private final CabDocumentoGeneradoRepo repo;

    @Override
    protected GenericRepo<CabDocumentoGenerado, Long> getRepo() {
        return repo;
    }

    public Page<CabDocumentoGenerado> getGeneralDocumentoGeneradoIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable) {
        return repo.getGeneralDocumentoGeneradoIA(filters, notEqualFilters, filtersFecha, pageable);
    }
}
