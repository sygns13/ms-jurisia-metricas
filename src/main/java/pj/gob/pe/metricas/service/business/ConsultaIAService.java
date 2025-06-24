package pj.gob.pe.metricas.service.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAMain1;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAMain2;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputDetailConsultaIA;
import pj.gob.pe.metricas.utils.responses.consultaia.ResponseConsultaIAMain1;
import pj.gob.pe.metricas.utils.responses.consultaia.ResponseConsultaIAMain2;

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

    ResponseConsultaIAMain1 getMainConsultaIA1(
            InputConsultaIAMain1 inputData) throws Exception;

    ResponseConsultaIAMain2 getMainConsultaIA2(
            InputConsultaIAMain2 inputData) throws Exception;
}
