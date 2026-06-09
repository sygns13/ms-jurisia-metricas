package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.repository.custom.DemandasCalificadasCustomRepo;

public interface DemandasCalificadasToKafkaRepo extends GenericRepo<DemandasCalificadasToKafka, Long>, DemandasCalificadasCustomRepo {
}
