package pj.gob.pe.metricas.service.externals;

import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.security.UserIdsRequest;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;
import pj.gob.pe.metricas.utils.responses.security.User;

import java.util.List;

public interface SecurityService {

    ResponseLogin GetSessionData(String SessionId);

    List<OutputConsultaIAExternal> Getusers(InputConsultaIAExternal inputConsultaIAExternal);

    List<User> GetListUsers(UserIdsRequest userIdsRequest);
}
