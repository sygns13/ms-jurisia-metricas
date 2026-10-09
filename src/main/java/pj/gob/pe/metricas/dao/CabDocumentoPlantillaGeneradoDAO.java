package pj.gob.pe.metricas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaDetalle;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaFiltros;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorDocumento;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorExpediente;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorInstancia;

import java.util.List;
import java.util.Map;

public interface CabDocumentoPlantillaGeneradoDAO extends GenericDAO<CabDocumentoPlantillaGenerado, Long> {

    CabDocumentoPlantillaGenerado findBySessionUID(String sessionUID);

    Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(
            Map<String, Object> filters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    List<CabDocumentoPlantillaGenerado> getListDocumentosPlantillaGenerados(
            Map<String, Object> filters,
            Map<String, Object> filtersFecha);

    Page<ResponseAdminDocPlantillaPorInstancia> reporteAdminPorInstancia(InputAdminDocPlantillaPorInstancia input, Pageable pageable);

    Page<ResponseAdminDocPlantillaPorExpediente> reporteAdminPorExpediente(InputAdminDocPlantillaFiltros input, Pageable pageable);

    Page<ResponseAdminDocPlantillaPorDocumento> reporteAdminPorDocumento(InputAdminDocPlantillaFiltros input, Pageable pageable);

    Page<CabDocumentoPlantillaGenerado> detalleAdmin(InputAdminDocPlantillaDetalle input, Pageable pageable);
}
