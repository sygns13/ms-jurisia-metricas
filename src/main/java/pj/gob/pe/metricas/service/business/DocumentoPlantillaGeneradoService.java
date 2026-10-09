package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.beans.DocumentoPlantillaGeneradoToKafka;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaDetalle;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaFiltros;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorDocumento;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorExpediente;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;
import pj.gob.pe.metricas.utils.inputs.docplantilla.InputDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.responses.docplantilla.ResponseResumenDocPlantilla;

import java.util.List;

public interface DocumentoPlantillaGeneradoService {

    void RegistrarDocumentoPlantillaGenerado(DocumentoPlantillaGeneradoToKafka evento) throws Exception;

    Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(String SessionId, InputDocumentoPlantillaGenerado inputData, Pageable pageable);

    List<DetDocumentoPlantillaVariable> getVariables(String SessionId, Long idCab) throws Exception;

    ResponseResumenDocPlantilla getResumen(String SessionId, InputDocumentoPlantillaGenerado inputData);

    Page<ResponseAdminDocPlantillaPorInstancia> reporteAdminPorInstancia(String SessionId, InputAdminDocPlantillaPorInstancia input, Pageable pageable);

    Page<ResponseAdminDocPlantillaPorExpediente> reporteAdminPorExpediente(String SessionId, InputAdminDocPlantillaFiltros input, Pageable pageable);

    Page<ResponseAdminDocPlantillaPorDocumento> reporteAdminPorDocumento(String SessionId, InputAdminDocPlantillaFiltros input, Pageable pageable);

    Page<CabDocumentoPlantillaGenerado> detalleAdmin(String SessionId, InputAdminDocPlantillaDetalle input, Pageable pageable);
}
