package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputMainDocGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseDocumentoGeneradoPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseMainSumarisimoPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseTotalDocGeneradosPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseTotalFiltersDocGeneradosPlantilla;

/**
 * Reportes de /v1/documento-generado-ia sobre CabDocumentoPlantillaGenerado (flujo por plantilla).
 * Misma lógica y contrato que {@link DocumentoGeneradoService} (que leía CabDocumentoGenerado).
 */
public interface DocumentoGeneradoPlantillaService {

    Page<ResponseDocumentoGeneradoPlantilla> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable);

    ResponseTotalDocGeneradosPlantilla getTotalDocGenerados(String SessionId) throws Exception;

    ResponseTotalFiltersDocGeneradosPlantilla getTotalDocGeneradosFilters(InputTotalesCabDocGenerado inputData, String SessionId) throws Exception;

    ResponseMainSumarisimoPlantilla getMainDocumentoGenerado(InputMainDocGenerado inputData, String SessionId) throws Exception;
}
