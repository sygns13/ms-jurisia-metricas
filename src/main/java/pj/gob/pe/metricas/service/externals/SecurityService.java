package pj.gob.pe.metricas.service.externals;

import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.util.List;

public interface SecurityService {

    ResponseLogin GetSessionData(String SessionId);

    List<OutputConsultaIAExternal> Getusers(InputConsultaIAExternal inputConsultaIAExternal);
}
