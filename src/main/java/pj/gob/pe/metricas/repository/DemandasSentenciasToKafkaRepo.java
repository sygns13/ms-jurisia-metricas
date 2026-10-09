package pj.gob.pe.metricas.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.repository.custom.DemandasSentenciasCustomRepo;

import java.util.List;

public interface DemandasSentenciasToKafkaRepo extends GenericRepo<DemandasSentenciasToKafka, Long>, DemandasSentenciasCustomRepo {

    // Todas las versiones de un expediente, de la más nueva a la más antigua.
    @Query("SELECT d FROM DemandasSentenciasToKafka d WHERE d.nUnico = :nUnico ORDER BY d.id DESC")
    List<DemandasSentenciasToKafka> listarPorNunico(@Param("nUnico") Long nUnico);
}
