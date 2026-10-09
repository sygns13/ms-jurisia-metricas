package pj.gob.pe.metricas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorInstancia;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorInstancia;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;

import java.util.List;
import java.util.Map;

public interface DemandasSentenciasToKafkaDAO extends GenericDAO<DemandasSentenciasToKafka, Long> {

    Page<DemandasSentenciasToKafka> getGeneralDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    Long getTotalDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters);

    Page<ResponseAdminDemandasPorInstancia> reporteAdminPorInstancia(
            InputAdminDemandasPorInstancia input,
            Pageable pageable);

    Page<ResponseAdminDemandasPorExpediente> reporteAdminPorExpediente(
            InputAdminDemandasPorExpediente input,
            Pageable pageable);

    List<DemandasSentenciasToKafka> listarPorNunico(Long nUnico);
}
