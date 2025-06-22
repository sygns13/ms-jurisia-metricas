package pj.gob.pe.metricas.service.externals;


import pj.gob.pe.metricas.utils.responses.judicial.DataEspecialidadDTO;
import pj.gob.pe.metricas.utils.responses.judicial.DataInstanciaDTO;
import pj.gob.pe.metricas.utils.responses.judicial.Documento;
import pj.gob.pe.metricas.utils.responses.judicial.TipoDocumento;

import java.util.List;

public interface JudicialService {

    List<DataInstanciaDTO> GetInstancias();
    List<DataEspecialidadDTO> GetEspecialidades();
    List<TipoDocumento> GetTipoDocumento();
    List<Documento> GetDocumento();
}
