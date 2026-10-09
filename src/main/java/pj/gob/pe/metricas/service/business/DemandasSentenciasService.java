package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorInstancia;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorInstancia;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.utils.inputs.demandassentencias.InputDemandasSentencias;

import java.util.List;

public interface DemandasSentenciasService {

    void RegistrarDemandaSentencia(DemandasSentenciasToKafka demandasSentenciasToKafka) throws Exception;

    Page<DemandasSentenciasToKafka> getDemandasSentencias(
            String SessionId,
            InputDemandasSentencias inputData,
            Pageable pageable);

    Long getTotalDemandasSentencias(String buscar, String SessionId) throws Exception;

    Page<ResponseAdminDemandasPorInstancia> reporteAdminPorInstancia(
            String SessionId,
            InputAdminDemandasPorInstancia inputData,
            Pageable pageable);

    Page<ResponseAdminDemandasPorExpediente> reporteAdminPorExpediente(
            String SessionId,
            InputAdminDemandasPorExpediente inputData,
            Pageable pageable);

    List<DemandasSentenciasToKafka> detalleAdminPorNunico(String SessionId, Long nUnico);
}
