package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.dao.DemandasCalificadasToKafkaDAO;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.service.business.DemandasCalificadasService;

@Service
@RequiredArgsConstructor
public class DemandasCalificadasServiceImpl implements DemandasCalificadasService {

    private final DemandasCalificadasToKafkaDAO demandasCalificadasToKafkaDAO;

    @Override
    public void RegistrarDemandaCalificada(DemandasCalificadasToKafka demandasCalificadasToKafka) throws Exception {
        // El mensaje llega con el id de consultaia; se descarta para que la BD de métricas
        // genere su propio id (AUTO_INCREMENT) y la operación sea un INSERT (no un merge/update).
        demandasCalificadasToKafka.setId(null);
        this.demandasCalificadasToKafkaDAO.registrar(demandasCalificadasToKafka);
    }
}
