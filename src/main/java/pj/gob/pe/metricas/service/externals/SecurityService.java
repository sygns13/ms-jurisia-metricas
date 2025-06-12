package pj.gob.pe.metricas.service.externals;

import pj.gob.pe.metricas.utils.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.ResponseLogin;

import java.util.List;

public interface SecurityService {

    ResponseLogin GetSessionData(String SessionId);

    List<OutputConsultaIAExternal> Getusers(InputConsultaIAExternal inputConsultaIAExternal);
}
