package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.repository.custom.DemandasSentenciasCustomRepo;

public interface DemandasSentenciasToKafkaRepo extends GenericRepo<DemandasSentenciasToKafka, Long>, DemandasSentenciasCustomRepo {
}
