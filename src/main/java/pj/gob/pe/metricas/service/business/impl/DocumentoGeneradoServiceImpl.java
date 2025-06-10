package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.dao.CabDocumentoGeneradoDAO;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoService;

@Service
@RequiredArgsConstructor
public class DocumentoGeneradoServiceImpl implements DocumentoGeneradoService {

    private final ConfigProperties configProperties;
    private final CabDocumentoGeneradoDAO cabDocumentoGeneradoDAO;

    @Override
    public void RegistrarDocumentoGenerado(CabDocumentoGenerado cabDocumentoGenerado) throws Exception {
        this.cabDocumentoGeneradoDAO.registrar(cabDocumentoGenerado);

    }
}
