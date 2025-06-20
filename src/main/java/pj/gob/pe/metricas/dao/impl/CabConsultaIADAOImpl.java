package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.CabConsultaIADAO;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.repository.CabConsultaIARepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CabConsultaIADAOImpl extends GenericDAOImpl<CabConsultaIA, Long> implements CabConsultaIADAO {

    private final CabConsultaIARepo repo;

    @Override
    protected GenericRepo<CabConsultaIA, Long> getRepo() {
        return repo;
    }

    public CabConsultaIA findBySessionUID(String sessionUID){
        return repo.findBySessionUID(sessionUID);
    }

    public Page<CabConsultaIA> getGeneralConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable){

        return repo.getGeneralConsultaIA(filters, notEqualFilters, filtersFecha, pageable);

    }

    // Additional methods specific to CabConsultaIADAO can be implemented here if needed.
}
