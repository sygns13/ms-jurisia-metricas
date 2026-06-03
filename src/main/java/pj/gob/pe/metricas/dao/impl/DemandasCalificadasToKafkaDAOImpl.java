package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DemandasCalificadasToKafkaDAO;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.repository.DemandasCalificadasToKafkaRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class DemandasCalificadasToKafkaDAOImpl extends GenericDAOImpl<DemandasCalificadasToKafka, Long> implements DemandasCalificadasToKafkaDAO {

    private final DemandasCalificadasToKafkaRepo repo;

    @Override
    protected GenericRepo<DemandasCalificadasToKafka, Long> getRepo() {
        return repo;
    }
}
