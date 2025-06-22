package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputMainDocGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainSumarisimo;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseTotalFiltersDocGenerados;

public interface DocumentoGeneradoService {

    void RegistrarDocumentoGenerado(CabDocumentoGenerado completions) throws Exception;

    Page<CabDocumentoGenerado> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable);

    Long getTotalDocGenerados(String buscar, String SessionId) throws Exception;

    ResponseTotalFiltersDocGenerados getTotalDocGeneradosFilters(InputTotalesCabDocGenerado inputData, String SessionId) throws Exception;

    ResponseMainSumarisimo getMainDocumentoGenerado(InputMainDocGenerado inputData, String SessionId) throws Exception;
}
