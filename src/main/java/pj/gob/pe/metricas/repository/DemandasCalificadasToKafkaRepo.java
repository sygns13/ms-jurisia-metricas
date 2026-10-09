package pj.gob.pe.metricas.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.repository.custom.DemandasCalificadasCustomRepo;

import java.util.List;

public interface DemandasCalificadasToKafkaRepo extends GenericRepo<DemandasCalificadasToKafka, Long>, DemandasCalificadasCustomRepo {

    // Todas las versiones de un expediente, de la más nueva a la más antigua.
    @Query("SELECT d FROM DemandasCalificadasToKafka d WHERE d.nUnico = :nUnico ORDER BY d.id DESC")
    List<DemandasCalificadasToKafka> listarPorNunico(@Param("nUnico") Long nUnico);
}
