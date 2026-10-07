package pj.gob.pe.metricas.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;

import java.util.List;
import java.util.Map;

public interface CabDocumentoPlantillaGeneradoCustomRepo {

    Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(
            Map<String, Object> filters,
            Map<String, Object> filtersFecha,
            Pageable pageable);

    List<CabDocumentoPlantillaGenerado> getListDocumentosPlantillaGenerados(
            Map<String, Object> filters,
            Map<String, Object> filtersFecha);
}
