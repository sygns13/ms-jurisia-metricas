package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.CabDocumentoPlantillaGeneradoDAO;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaDetalle;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaFiltros;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorDocumento;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorExpediente;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.repository.CabDocumentoPlantillaGeneradoRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CabDocumentoPlantillaGeneradoDAOImpl extends GenericDAOImpl<CabDocumentoPlantillaGenerado, Long> implements CabDocumentoPlantillaGeneradoDAO {

    private final CabDocumentoPlantillaGeneradoRepo repo;

    @Override
    protected GenericRepo<CabDocumentoPlantillaGenerado, Long> getRepo() {
        return repo;
    }

    @Override
    public CabDocumentoPlantillaGenerado findBySessionUID(String sessionUID) {
        return repo.findFirstBySessionUID(sessionUID).orElse(null);
    }

    @Override
    public Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(
            Map<String, Object> filters, Map<String, Object> filtersFecha, Pageable pageable) {
        return repo.getDocumentosPlantillaGenerados(filters, filtersFecha, pageable);
    }

    @Override
    public List<CabDocumentoPlantillaGenerado> getListDocumentosPlantillaGenerados(
            Map<String, Object> filters, Map<String, Object> filtersFecha) {
        return repo.getListDocumentosPlantillaGenerados(filters, filtersFecha);
    }

    @Override
    public Page<ResponseAdminDocPlantillaPorInstancia> reporteAdminPorInstancia(InputAdminDocPlantillaPorInstancia input, Pageable pageable) {
        return repo.reporteAdminPorInstancia(input, pageable);
    }

    @Override
    public Page<ResponseAdminDocPlantillaPorExpediente> reporteAdminPorExpediente(InputAdminDocPlantillaFiltros input, Pageable pageable) {
        return repo.reporteAdminPorExpediente(input, pageable);
    }

    @Override
    public Page<ResponseAdminDocPlantillaPorDocumento> reporteAdminPorDocumento(InputAdminDocPlantillaFiltros input, Pageable pageable) {
        return repo.reporteAdminPorDocumento(input, pageable);
    }

    @Override
    public Page<CabDocumentoPlantillaGenerado> detalleAdmin(InputAdminDocPlantillaDetalle input, Pageable pageable) {
        return repo.detalleAdmin(input, pageable);
    }
}
