package pj.gob.pe.metricas.service.business;

import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;

public interface DemandasCalificadasService {

    void RegistrarDemandaCalificada(DemandasCalificadasToKafka demandasCalificadasToKafka) throws Exception;
}
