package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.SIJCabConsultaIADAO;
import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;
import pj.gob.pe.metricas.repository.GenericRepo;
import pj.gob.pe.metricas.repository.SIJCabConsultaIARepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SIJCabConsultaIADAOImpl extends GenericDAOImpl<SIJCabConsultaIA, Long> implements SIJCabConsultaIADAO {

    private final SIJCabConsultaIARepo repo;

    @Override
    protected GenericRepo<SIJCabConsultaIA, Long> getRepo() {
        return repo;
    }

    @Override
    public List<SIJCabConsultaIA> getGeneralConsultaIA(Map<String, Object> filters, Map<String, Object> notEqualFilters, Map<String, Object> filtersFecha) {
        return repo.getGeneralConsultaIA(filters, notEqualFilters, filtersFecha);
    }
}
