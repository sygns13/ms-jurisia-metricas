package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.utils.InputConsultaIA;

import java.util.Map;

public interface ConsultaIAService {

    void RegistrarConsultaIA(Completions completions) throws Exception;

    Page<CabConsultaIA> getGeneralConsultaIA(
            String SessionId,
            InputConsultaIA inputData,
            Pageable pageable);
}
