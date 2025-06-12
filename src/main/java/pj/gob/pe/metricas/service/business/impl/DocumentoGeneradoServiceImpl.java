package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.dao.CabDocumentoGeneradoDAO;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentoGeneradoServiceImpl implements DocumentoGeneradoService {

    private final SecurityService securityService;
    private final ConfigProperties configProperties;
    private final CabDocumentoGeneradoDAO cabDocumentoGeneradoDAO;

    @Override
    public void RegistrarDocumentoGenerado(CabDocumentoGenerado cabDocumentoGenerado) throws Exception {
        this.cabDocumentoGeneradoDAO.registrar(cabDocumentoGenerado);

    }

    @Override
    public Page<CabDocumentoGenerado> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable) {

        String errorValidacion = "";

        if(SessionId == null || SessionId.isEmpty()){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        ResponseLogin responseLogin = securityService.GetSessionData(SessionId);

        if(responseLogin == null || !responseLogin.isSuccess() || !responseLogin.isItemFound() || responseLogin.getUser() == null){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        Map<String, Object> filters = new HashMap<>();
        if(Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL)) {
            filters.put("userId", responseLogin.getUser().getIdUser());
        } else {
            if(inputData.getIdUser() != null && inputData.getIdUser() > 0) {
                filters.put("userId", inputData.getIdUser());
            } else{
                //Consultar por usuarios
                boolean consultaPorUsuarios = false;

                InputConsultaIAExternal inputConsultaIAExternal = new InputConsultaIAExternal();

                if(inputData.getDocumento() != null && !inputData.getDocumento().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setDocumento(inputData.getDocumento());
                }
                if(inputData.getNombres() != null && !inputData.getNombres().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setNombres(inputData.getNombres());
                }
                if(inputData.getApellidos() != null && !inputData.getApellidos().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setApellidos(inputData.getApellidos());
                }
                if(inputData.getCargo() != null && !inputData.getCargo().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setCargo(inputData.getCargo());
                }
                if(inputData.getUsername() != null && !inputData.getUsername().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setUsername(inputData.getUsername());
                }
                if(inputData.getEmail() != null && !inputData.getEmail().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setEmail(inputData.getEmail());
                }

                if(consultaPorUsuarios){
                    List<OutputConsultaIAExternal> listUsers = securityService.Getusers(inputConsultaIAExternal);

                    if(listUsers == null || listUsers.isEmpty()) {
                        Page<CabDocumentoGenerado> response = new PageImpl<>(Collections.emptyList(), pageable, 0);
                        return response;
                    }

                    // Agregar los IDs de los usuarios encontrados
                    filters.put("list_userId", listUsers.stream().map(OutputConsultaIAExternal::getId).toList());
                }
            }
        }


        if(inputData.getTypedoc() != null && !inputData.getTypedoc().isEmpty()) {
            filters.put("typedoc", inputData.getTypedoc());
        }
        if(inputData.getCodSede() != null && !inputData.getCodSede().isEmpty()) {
            filters.put("codSede", inputData.getCodSede());
        }
        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()) {
            filters.put("codInstancia", inputData.getCodInstancia());
        }
        if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
            filters.put("codEspecialidad", inputData.getCodEspecialidad());
        }
        if(inputData.getCodMateria() != null && !inputData.getCodMateria().isEmpty()) {
            filters.put("codMateria", inputData.getCodMateria());
        }
        if(inputData.getNumeroExpediente() != null && !inputData.getNumeroExpediente().isEmpty()) {
            filters.put("codNumero", "0".repeat(5 - inputData.getNumeroExpediente().length()) + inputData.getNumeroExpediente());
        }
        if(inputData.getYearExpediente() != null && !inputData.getYearExpediente().isEmpty()) {
            filters.put("codYear", inputData.getYearExpediente());
        }
        if(inputData.getIdDocumento() != null && inputData.getIdDocumento() > 0) {
            filters.put("idDocumento", inputData.getIdDocumento());
        }
        if(inputData.getIdTipoDocumento() != null && inputData.getIdTipoDocumento() > 0) {
            filters.put("idTipoDocumento", inputData.getIdTipoDocumento());
        }
        if(inputData.getNUnico() != null && inputData.getNUnico() > 0) {
            filters.put("nUnico", inputData.getNUnico());
        }
        if(inputData.getXFormato() != null && !inputData.getXFormato().isEmpty()) {
            filters.put("xFormato", inputData.getXFormato());
        }
        if(inputData.getDniDemandante() != null && !inputData.getDniDemandante().isEmpty()) {
            filters.put("dniDemandante", inputData.getDniDemandante());
        }
        if(inputData.getDniDemandado() != null && !inputData.getDniDemandado().isEmpty()) {
            filters.put("dniDemandado", inputData.getDniDemandado());
        }
        if(inputData.getTemplateCode() != null && !inputData.getTemplateCode().isEmpty()) {
            filters.put("templateCode", inputData.getTemplateCode());
        }
        if(inputData.getTemplateID() != null && inputData.getTemplateID() > 0) {
            filters.put("templateID", inputData.getTemplateID());
        }

        Map<String, Object> filtersFecha = new HashMap<>();
        Map<String, Object> filtersNotEquals = new HashMap<>();
        if(inputData.getFechaInicio() != null && inputData.getFechaFin() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        } else if(inputData.getFechaInicio() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
        } else if(inputData.getFechaFin() != null) {
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        }

        Page<CabDocumentoGenerado> response = cabDocumentoGeneradoDAO.getGeneralDocumentoGeneradoIA(filters, filtersNotEquals, filtersFecha, pageable);

        return response;
    }
}
