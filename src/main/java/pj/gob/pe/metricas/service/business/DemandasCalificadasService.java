package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.utils.inputs.demandascalificadas.InputDemandasCalificadas;

public interface DemandasCalificadasService {

    void RegistrarDemandaCalificada(DemandasCalificadasToKafka demandasCalificadasToKafka) throws Exception;

    Page<DemandasCalificadasToKafka> getDemandasCalificadas(
            String SessionId,
            InputDemandasCalificadas inputData,
            Pageable pageable);

    Long getTotalDemandasCalificadas(String buscar, String SessionId) throws Exception;
}
