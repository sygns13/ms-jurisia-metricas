package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.dao.CabConsultaIADAO;
import pj.gob.pe.metricas.dao.DetailConsultaIADAO;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.service.business.ConsultaIAService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.*;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputDetailConsultaIA;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConsultaIAServiceImpl implements ConsultaIAService {

    private final SecurityService securityService;
    private final ConfigProperties configProperties;
    private final CabConsultaIADAO cabConsultaIADAO;
    private final DetailConsultaIADAO detailConsultaIADAO;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaIAServiceImpl.class);

    @Override
    public void RegistrarConsultaIA(Completions completions) throws Exception {

        //Verificar si la conversacion ya existe
        CabConsultaIA completionsExistente = cabConsultaIADAO.findBySessionUID(completions.getSessionUID());
        LocalDateTime fechaActualTime = LocalDateTime.now();

        if(completionsExistente == null || completionsExistente.getId() == null) {

            //Registrar la cabecera de la consulta
            CabConsultaIA cabConsultaIA = new CabConsultaIA();

            cabConsultaIA.setUserId(completions.getUserId());
            cabConsultaIA.setModel(completions.getModel());
            cabConsultaIA.setCountMessages(Constantes.CANTIDAD_UNIDAD_INTEGER);
            cabConsultaIA.setFirstSendMessage(completions.getRoleUser());
            cabConsultaIA.setLastSendMessage(completions.getRoleUser());
            cabConsultaIA.setFirstResponseMessage(completions.getRoleContent());
            cabConsultaIA.setLastResponseMessage(completions.getRoleContent());
            cabConsultaIA.setSessionUID(completions.getSessionUID());
            cabConsultaIA.setRegDate(fechaActualTime.toLocalDate());
            cabConsultaIA.setRegDatetime(fechaActualTime);
            cabConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));


            //Guardar la cabecera
            cabConsultaIADAO.registrar(cabConsultaIA);
        } else {
            //Actualizar la cabecera de la consulta
            completionsExistente.setCountMessages(completionsExistente.getCountMessages() + Constantes.CANTIDAD_UNIDAD_INTEGER);
            completionsExistente.setLastSendMessage(completions.getRoleUser());
            completionsExistente.setLastResponseMessage(completions.getRoleContent());
            completionsExistente.setUpdDate(fechaActualTime.toLocalDate());
            completionsExistente.setUpdDatetime(fechaActualTime);
            completionsExistente.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

            //Guardar la cabecera actualizada
            cabConsultaIADAO.modificar(completionsExistente);
        }

        DetailConsultaIA detailConsultaIA = new DetailConsultaIA();

        detailConsultaIA.setUserId(completions.getUserId());
        detailConsultaIA.setModel(completions.getModel());
        detailConsultaIA.setRoleSystem(completions.getRoleSystem());
        detailConsultaIA.setSendMessage(completions.getRoleUser());
        detailConsultaIA.setTemperature(completions.getTemperature());
        detailConsultaIA.setFechaSend(completions.getFechaSend());
        detailConsultaIA.setFechaResponse(completions.getFechaResponse());
        detailConsultaIA.setIdGpt(completions.getIdGpt());
        detailConsultaIA.setObject(completions.getObject());
        detailConsultaIA.setCreated(completions.getCreated());
        detailConsultaIA.setModelResponse(completions.getModelResponse());
        detailConsultaIA.setRoleResponse(completions.getRoleResponse());
        detailConsultaIA.setResponseMessage(completions.getRoleContent());
        detailConsultaIA.setRefusal(completions.getRefusal());
        detailConsultaIA.setLogprobs(completions.getLogprobs());
        detailConsultaIA.setFinishReason(completions.getFinishReason());
        detailConsultaIA.setPromptTokens(completions.getPromptTokens());
        detailConsultaIA.setCompletionTokens(completions.getCompletionTokens());
        detailConsultaIA.setTotalTokens(completions.getTotalTokens());
        detailConsultaIA.setCachedTokens(completions.getCachedTokens());
        detailConsultaIA.setAudioTokens(completions.getAudioTokens());
        detailConsultaIA.setCompletionReasoningTokens(completions.getCompletionReasoningTokens());
        detailConsultaIA.setCompletionAudioTokens(completions.getCompletionAudioTokens());
        detailConsultaIA.setCompletionAceptedTokens(completions.getCompletionAceptedTokens());
        detailConsultaIA.setCompletionRejectedTokens(completions.getCompletionRejectedTokens());
        detailConsultaIA.setServiceTier(completions.getServiceTier());
        detailConsultaIA.setSystemFingerprint(completions.getSystemFingerprint());
        detailConsultaIA.setConfigurationsId(completions.getConfigurationsId());
        detailConsultaIA.setSessionUID(completions.getSessionUID());
        detailConsultaIA.setStatus(completions.getStatus());
        detailConsultaIA.setRegDate(fechaActualTime.toLocalDate());
        detailConsultaIA.setRegDatetime(fechaActualTime);
        detailConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        detailConsultaIADAO.registrar(detailConsultaIA);
    }

    @Override
    public Page<CabConsultaIA> getGeneralConsultaIA(String SessionId, InputConsultaIA inputData, Pageable pageable) {

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
        if(Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)) {
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
                        Page<CabConsultaIA> response = new PageImpl<>(Collections.emptyList(), pageable, 0);
                        return response;
                    }

                    // Agregar los IDs de los usuarios encontrados
                    filters.put("list_userId", listUsers.stream().map(OutputConsultaIAExternal::getId).toList());
                }
            }
        }

        if(inputData.getModel() != null && !inputData.getModel().isEmpty()) {
            filters.put("model", inputData.getModel());
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

        Page<CabConsultaIA> response = cabConsultaIADAO.getGeneralConsultaIA(filters, filtersNotEquals, filtersFecha, pageable);

        return response;
    }

    @Override
    public Page<DetailConsultaIA> getDetailConsultaIA(String SessionId, InputDetailConsultaIA inputData, Pageable pageable) {

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
        if(Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)) {
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
                        Page<DetailConsultaIA> response = new PageImpl<>(Collections.emptyList(), pageable, 0);
                        return response;
                    }

                    // Agregar los IDs de los usuarios encontrados
                    filters.put("list_userId", listUsers.stream().map(OutputConsultaIAExternal::getId).toList());
                }
            }
        }


        if(inputData.getModel() != null && !inputData.getModel().isEmpty()) {
            filters.put("model", inputData.getModel());
        }

        if(inputData.getSessionUID() != null && !inputData.getSessionUID().isEmpty()) {
            filters.put("sessionUID", inputData.getSessionUID());
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

        Page<DetailConsultaIA> response = detailConsultaIADAO.getDetailConsultaIA(filters, filtersNotEquals, filtersFecha, pageable);

        return response;
    }
}
