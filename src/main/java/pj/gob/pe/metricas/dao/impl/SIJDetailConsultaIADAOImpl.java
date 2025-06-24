package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.SIJDetailConsultaIADAO;
import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;
import pj.gob.pe.metricas.repository.GenericRepo;
import pj.gob.pe.metricas.repository.SIJDetailConsultaIARepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SIJDetailConsultaIADAOImpl extends GenericDAOImpl<SIJDetailConsultaIA, Long> implements SIJDetailConsultaIADAO {

    private final SIJDetailConsultaIARepo repo;

    @Override
    protected GenericRepo<SIJDetailConsultaIA, Long> getRepo() {
        return repo;
    }

    @Override
    public List<SIJDetailConsultaIA> getDetailConsultaIA(Map<String, Object> filters, Map<String, Object> notEqualFilters, Map<String, Object> filtersFecha) {
        return repo.getDetailConsultaIA(filters, notEqualFilters, filtersFecha);
    }
}
