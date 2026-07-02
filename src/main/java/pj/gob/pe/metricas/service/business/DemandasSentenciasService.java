package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.utils.inputs.demandassentencias.InputDemandasSentencias;

public interface DemandasSentenciasService {

    void RegistrarDemandaSentencia(DemandasSentenciasToKafka demandasSentenciasToKafka) throws Exception;

    Page<DemandasSentenciasToKafka> getDemandasSentencias(
            String SessionId,
            InputDemandasSentencias inputData,
            Pageable pageable);

    Long getTotalDemandasSentencias(String buscar, String SessionId) throws Exception;
}
