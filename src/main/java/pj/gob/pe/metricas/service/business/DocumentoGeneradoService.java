package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.utils.InputDocumentoGeneradoIA;

public interface DocumentoGeneradoService {

    void RegistrarDocumentoGenerado(CabDocumentoGenerado completions) throws Exception;

    Page<CabDocumentoGenerado> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable);
}
