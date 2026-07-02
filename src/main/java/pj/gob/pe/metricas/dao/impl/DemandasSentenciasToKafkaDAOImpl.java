package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DemandasSentenciasToKafkaDAO;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.repository.DemandasSentenciasToKafkaRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DemandasSentenciasToKafkaDAOImpl extends GenericDAOImpl<DemandasSentenciasToKafka, Long> implements DemandasSentenciasToKafkaDAO {

    private final DemandasSentenciasToKafkaRepo repo;

    @Override
    protected GenericRepo<DemandasSentenciasToKafka, Long> getRepo() {
        return repo;
    }

    @Override
    public Page<DemandasSentenciasToKafka> getGeneralDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable) {
        return repo.getGeneralDemandasSentencias(filters, notEqualFilters, filtersFecha, pageable);
    }

    @Override
    public Long getTotalDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters) {
        return repo.getTotalDemandasSentencias(filters, notEqualFilters);
    }
}
