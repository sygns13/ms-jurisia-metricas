package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputDetailConsultaIA;

public interface ConsultaIAService {

    void RegistrarConsultaIA(Completions completions) throws Exception;

    Page<CabConsultaIA> getGeneralConsultaIA(
            String SessionId,
            InputConsultaIA inputData,
            Pageable pageable);

    Page<DetailConsultaIA> getDetailConsultaIA(
            String SessionId,
            InputDetailConsultaIA inputData,
            Pageable pageable);
}
