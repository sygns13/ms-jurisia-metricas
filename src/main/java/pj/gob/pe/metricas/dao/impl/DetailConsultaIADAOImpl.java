package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DetailConsultaIADAO;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.repository.DetailConsultaIARepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DetailConsultaIADAOImpl extends GenericDAOImpl<DetailConsultaIA, Long> implements DetailConsultaIADAO {

    private final DetailConsultaIARepo repo;

    @Override
    protected GenericRepo<DetailConsultaIA, Long> getRepo() {
        return repo;
    }

    public Page<DetailConsultaIA> getDetailConsultaIA(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable) {
        return repo.getDetailConsultaIA(filters, notEqualFilters, filtersFecha, pageable);
    }
}
