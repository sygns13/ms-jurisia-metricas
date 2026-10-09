package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DemandasCalificadasToKafkaDAO;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorInstancia;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorInstancia;
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.repository.DemandasCalificadasToKafkaRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DemandasCalificadasToKafkaDAOImpl extends GenericDAOImpl<DemandasCalificadasToKafka, Long> implements DemandasCalificadasToKafkaDAO {

    private final DemandasCalificadasToKafkaRepo repo;

    @Override
    protected GenericRepo<DemandasCalificadasToKafka, Long> getRepo() {
        return repo;
    }

    @Override
    public Page<DemandasCalificadasToKafka> getGeneralDemandasCalificadas(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha,
            Pageable pageable) {
        return repo.getGeneralDemandasCalificadas(filters, notEqualFilters, filtersFecha, pageable);
    }

    @Override
    public Long getTotalDemandasCalificadas(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters) {
        return repo.getTotalDemandasCalificadas(filters, notEqualFilters);
    }

    @Override
    public Page<ResponseAdminDemandasPorInstancia> reporteAdminPorInstancia(
            InputAdminDemandasPorInstancia input,
            Pageable pageable) {
        return repo.reporteAdminPorInstancia(input, pageable);
    }

    @Override
    public Page<ResponseAdminDemandasPorExpediente> reporteAdminPorExpediente(
            InputAdminDemandasPorExpediente input,
            Pageable pageable) {
        return repo.reporteAdminPorExpediente(input, pageable);
    }

    @Override
    public List<DemandasCalificadasToKafka> listarPorNunico(Long nUnico) {
        return repo.listarPorNunico(nUnico);
    }
}
