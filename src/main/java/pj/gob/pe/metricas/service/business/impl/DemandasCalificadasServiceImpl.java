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
        this.demandasCalificadasToKafkaDAO.registrar(demandasCalificadasToKafka);
    }
}
