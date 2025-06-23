package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.SIJDetailConsultaIADAO;
import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;
import pj.gob.pe.metricas.repository.GenericRepo;
import pj.gob.pe.metricas.repository.SIJDetailConsultaIARepo;

@Repository
@RequiredArgsConstructor
public class SIJDetailConsultaIADAOImpl extends GenericDAOImpl<SIJDetailConsultaIA, Long> implements SIJDetailConsultaIADAO {

    private final SIJDetailConsultaIARepo repo;

    @Override
    protected GenericRepo<SIJDetailConsultaIA, Long> getRepo() {
        return repo;
    }
}
