package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.utils.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.ResponseTotalFiltersDocGenerados;

public interface DocumentoGeneradoService {

    void RegistrarDocumentoGenerado(CabDocumentoGenerado completions) throws Exception;

    Page<CabDocumentoGenerado> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable);

    Long getTotalDocGenerados(String buscar, String SessionId) throws Exception;

    ResponseTotalFiltersDocGenerados getTotalDocGeneradosFilters(InputTotalesCabDocGenerado inputData, String SessionId) throws Exception;
}
