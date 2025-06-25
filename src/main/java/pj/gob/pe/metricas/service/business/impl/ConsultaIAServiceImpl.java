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
import pj.gob.pe.metricas.dao.SIJCabConsultaIADAO;
import pj.gob.pe.metricas.dao.SIJDetailConsultaIADAO;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;
import pj.gob.pe.metricas.model.entities.SIJDetailConsultaIA;
import pj.gob.pe.metricas.service.business.ConsultaIAService;
import pj.gob.pe.metricas.service.externals.JudicialService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.*;
import pj.gob.pe.metricas.utils.inputs.consultaia.*;
import pj.gob.pe.metricas.utils.inputs.security.UserIdsRequest;
import pj.gob.pe.metricas.utils.responses.consultaia.*;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainDoc;
import pj.gob.pe.metricas.utils.responses.judicial.DataEspecialidadDTO;
import pj.gob.pe.metricas.utils.responses.judicial.DataInstanciaDTO;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;
import pj.gob.pe.metricas.utils.responses.security.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaIAServiceImpl implements ConsultaIAService {

    private final SecurityService securityService;
    private final ConfigProperties configProperties;
    private final CabConsultaIADAO cabConsultaIADAO;
    private final DetailConsultaIADAO detailConsultaIADAO;
    private final SIJCabConsultaIADAO sIJCabConsultaIADAO;
    private final SIJDetailConsultaIADAO sIJDetailConsultaIADAO;
    private final JudicialService judicialService;

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

            //Guardar Data de Sedes e Instancias de Usuarios
                completions.getSedes().forEach(sede -> {
                    sede.getInstancias().forEach(instancia -> {
                        SIJCabConsultaIA sIJCabConsultaIA = new SIJCabConsultaIA();

                        sIJCabConsultaIA.setUserId(completions.getUserId());
                        sIJCabConsultaIA.setCodSede(sede.getCodSede());
                        sIJCabConsultaIA.setCodInstancia(instancia.getCodInstancia());
                        sIJCabConsultaIA.setSede(sede.getSede());
                        sIJCabConsultaIA.setInstancia(instancia.getInstancia());
                        sIJCabConsultaIA.setSessionUID(completions.getSessionUID());
                        sIJCabConsultaIA.setRegDate(fechaActualTime.toLocalDate());
                        sIJCabConsultaIA.setRegDatetime(fechaActualTime);
                        sIJCabConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

                        try {
                            sIJCabConsultaIADAO.registrar(sIJCabConsultaIA);
                        } catch (Exception e) {
                            //throw new )RuntimeException(e);
                            logger.debug(e.getMessage());
                        }
                    });
                });


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

        DetailConsultaIA finalDetailConsultaIA = detailConsultaIADAO.registrar(detailConsultaIA);

        //Guardar Data de Sedes e Instancias de Usuarios
        //DetailConsultaIA finalDetailConsultaIA = detailConsultaIA;
        completions.getSedes().forEach(sede -> {
            sede.getInstancias().forEach(instancia -> {
                SIJDetailConsultaIA sIJDetConsultaIA = new SIJDetailConsultaIA();

                sIJDetConsultaIA.setUserId(completions.getUserId());
                sIJDetConsultaIA.setDetailConsultaId(finalDetailConsultaIA.getId());
                sIJDetConsultaIA.setCodSede(sede.getCodSede());
                sIJDetConsultaIA.setCodInstancia(instancia.getCodInstancia());
                sIJDetConsultaIA.setSede(sede.getSede());
                sIJDetConsultaIA.setInstancia(instancia.getInstancia());
                sIJDetConsultaIA.setSessionUID(finalDetailConsultaIA.getSessionUID());
                sIJDetConsultaIA.setRegDate(fechaActualTime.toLocalDate());
                sIJDetConsultaIA.setRegDatetime(fechaActualTime);
                sIJDetConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

                try {
                    sIJDetailConsultaIADAO.registrar(sIJDetConsultaIA);
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    logger.debug(e.getMessage());
                }
            });
        });


        //Guardar la cabecera actualizada



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

    @Override
    public ResponseConsultaIAMain1 getMainConsultaIA1(InputConsultaIAMain1 inputData) throws Exception {
        ResponseConsultaIAMain1 response = new ResponseConsultaIAMain1();

        response.setConsultas(0L);

        List<DataInstanciaDTO> instancias = judicialService.GetInstancias();
        List<DataEspecialidadDTO> especialidadesAll = judicialService.GetEspecialidades();

        List<DataEspecialidadDTO> especialidades = new ArrayList<>();

        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()){
            response.setCodInstancia(inputData.getCodInstancia());
            Optional<DataInstanciaDTO> instanciaOpt = instancias.stream()
                    .filter(instancia -> instancia.getCodigoInstancia().equals(inputData.getCodInstancia()))
                    .findFirst();
            if(instanciaOpt.isPresent()){
                response.setInstancia(instanciaOpt.get().getInstancia());
            } else {
                response.setInstancia("");
            }
        }
        else{
            response.setCodInstancia("0");
            response.setInstancia("Todos");
        }

        Map<String, Object> filters = new HashMap<>();
        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()) {
            filters.put("codInstancia", inputData.getCodInstancia());
        }
        if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
            //filters.put("codEspecialidad", inputData.getCodEspecialidad());
            especialidadesAll.forEach(especialidad -> {
                if(inputData.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())){
                    DataEspecialidadDTO dataEspecialidadDTO = new DataEspecialidadDTO();

                    dataEspecialidadDTO.setCodigoEspecialidad(especialidad.getCodigoEspecialidad());
                    dataEspecialidadDTO.setEspecialidad(especialidad.getEspecialidad());
                    dataEspecialidadDTO.setCodigoInstancia(especialidad.getCodigoInstancia());
                    dataEspecialidadDTO.setCodigoCodEspecialidad(especialidad.getCodigoCodEspecialidad());

                    especialidades.add(dataEspecialidadDTO);
                }
            });
        } else {
            especialidades.addAll(especialidadesAll);
        }
        if(inputData.getIdUser() != null && inputData.getIdUser() > 0) {
            filters.put("userId", inputData.getIdUser());

            UserIdsRequest userIdsRequest = new UserIdsRequest();
            List<Long> idUsers = new ArrayList<>();
            idUsers.add(inputData.getIdUser());
            userIdsRequest.setIdUsers(idUsers);

            List<User> users = securityService.GetListUsers(userIdsRequest);

            if(users != null && !users.isEmpty()){
                response.setDocumento(users.get(0).getDocumento());
                response.setNombres(users.get(0).getNombres());
                response.setApellidos(users.get(0).getApellidos());
                response.setUsername(users.get(0).getUsername());
                response.setCargoUsuario(users.get(0).getCargo());
            }

        } else{
            response.setIdUser(0L);
            response.setNombres("Todos");
        }

        Map<String, Object> filtersFecha = new HashMap<>();
        if(inputData.getFechaInicio() != null && inputData.getFechaFin() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        } else if(inputData.getFechaInicio() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
        } else if(inputData.getFechaFin() != null) {
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        }

        Map<String, Object> filtersNotEquals = new HashMap<>();
        List<SIJDetailConsultaIA> sIJDetailConsultaIA = sIJDetailConsultaIADAO.getDetailConsultaIA(filters, filtersNotEquals, filtersFecha);
        AtomicReference<Long> totalConsultas = new AtomicReference<>(0L);
        Long totalConsultasReal = 0L;

        if(response.getCodInstancia().equals("0")) {
            totalConsultasReal = sIJDetailConsultaIA.stream().count();
        } else {
            totalConsultasReal = sIJDetailConsultaIA.stream().filter(consulta -> consulta.getCodInstancia().equals(response.getCodInstancia())).count();
        }

        List<ResponseMainEspecialidad1> responseEspecialidades = new ArrayList<>();

        especialidades.forEach(especialidad -> {
            if(response.getCodInstancia().equals("0") || response.getCodInstancia().equals(especialidad.getCodigoInstancia())){
                ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                responseMainEspecialidad1.setCodEspecialidad(especialidad.getCodigoEspecialidad());
                responseMainEspecialidad1.setEspecialidad(especialidad.getEspecialidad());

                if(response.getCodInstancia().equals("0")) {
                    responseMainEspecialidad1.setConsultas(sIJDetailConsultaIA.stream().filter(consulta ->
                            consulta.getDetailConsultaIA().getSendMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase()) ||
                            consulta.getDetailConsultaIA().getResponseMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase())
                    ).count());
                } else {
                    responseMainEspecialidad1.setConsultas(sIJDetailConsultaIA.stream().filter(consulta ->
                                    consulta.getCodInstancia().equals(response.getCodInstancia()) && (
                                        consulta.getDetailConsultaIA().getSendMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase()) ||
                                        consulta.getDetailConsultaIA().getResponseMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase())
                                    )
                    ).count());
                }

                responseEspecialidades.add(responseMainEspecialidad1);
                totalConsultas.set(totalConsultas.get() + responseMainEspecialidad1.getConsultas());
            }
        });

        if(totalConsultasReal > totalConsultas.get()){
            if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
                response.setConsultas(totalConsultas.get());
            } else {
                ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                responseMainEspecialidad1.setCodEspecialidad("0");
                responseMainEspecialidad1.setEspecialidad("OTROS");
                responseMainEspecialidad1.setConsultas(totalConsultasReal - totalConsultas.get());

                responseEspecialidades.add(responseMainEspecialidad1);

                response.setConsultas(totalConsultasReal);
            }
        } else{
            if(inputData.getCodEspecialidad() == null || inputData.getCodEspecialidad().isEmpty()) {
                ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                responseMainEspecialidad1.setCodEspecialidad("0");
                responseMainEspecialidad1.setEspecialidad("OTROS");
                responseMainEspecialidad1.setConsultas(0L);

                responseEspecialidades.add(responseMainEspecialidad1);
            }

            response.setConsultas(totalConsultas.get());
        }

        response.setEspecialidades(responseEspecialidades);

        return response;
    }

    @Override
    public ResponseConsultaIAMain2 getMainConsultaIA2(InputConsultaIAMain2 inputData) throws Exception {
        ResponseConsultaIAMain2 response = new ResponseConsultaIAMain2();

        response.setConsultas(0L);

        List<DataInstanciaDTO> instancias = judicialService.GetInstancias();
        List<DataEspecialidadDTO> especialidadesAll = judicialService.GetEspecialidades();

        List<DataEspecialidadDTO> especialidades = new ArrayList<>();

        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()){
            response.setCodInstancia(inputData.getCodInstancia());
            Optional<DataInstanciaDTO> instanciaOpt = instancias.stream()
                    .filter(instancia -> instancia.getCodigoInstancia().equals(inputData.getCodInstancia()))
                    .findFirst();
            if(instanciaOpt.isPresent()){
                response.setInstancia(instanciaOpt.get().getInstancia());
            } else {
                response.setInstancia("");
            }
        }
        else{
            response.setCodInstancia("0");
            response.setInstancia("Todos");
        }

        Map<String, Object> filters = new HashMap<>();
        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()) {
            filters.put("codInstancia", inputData.getCodInstancia());
        }
        if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
            //filters.put("codEspecialidad", inputData.getCodEspecialidad());
            especialidadesAll.forEach(especialidad -> {
                if(inputData.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())){
                    DataEspecialidadDTO dataEspecialidadDTO = new DataEspecialidadDTO();

                    dataEspecialidadDTO.setCodigoEspecialidad(especialidad.getCodigoEspecialidad());
                    dataEspecialidadDTO.setEspecialidad(especialidad.getEspecialidad());
                    dataEspecialidadDTO.setCodigoInstancia(especialidad.getCodigoInstancia());
                    dataEspecialidadDTO.setCodigoCodEspecialidad(especialidad.getCodigoCodEspecialidad());

                    especialidades.add(dataEspecialidadDTO);
                }
            });
        } else {
            especialidades.addAll(especialidadesAll);
        }
        if(inputData.getIdUser() != null && inputData.getIdUser() > 0) {
            filters.put("userId", inputData.getIdUser());
        }

        Map<String, Object> filtersFecha = new HashMap<>();
        if(inputData.getFechaInicio() != null && inputData.getFechaFin() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        } else if(inputData.getFechaInicio() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
        } else if(inputData.getFechaFin() != null) {
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        }

        Map<String, Object> filtersNotEquals = new HashMap<>();
        List<SIJDetailConsultaIA> sIJDetailConsultaIA = sIJDetailConsultaIADAO.getDetailConsultaIA(filters, filtersNotEquals, filtersFecha);
        AtomicReference<Long> totalConsultas = new AtomicReference<>(0L);


        List<ResponseMainUsuarios> responseMainUsuariosAll = new ArrayList<>();
        sIJDetailConsultaIA.forEach(consulta -> {
            ResponseMainUsuarios responseMainUsuario = new ResponseMainUsuarios();
            responseMainUsuario.setIdUser(consulta.getUserId());

            responseMainUsuariosAll.add(responseMainUsuario);
        });

        List<ResponseMainUsuarios> responseMainUsuarios = responseMainUsuariosAll.stream()
                .distinct()
                .collect(Collectors.toList());

        UserIdsRequest userIdsRequest = new UserIdsRequest();
        List<Long> idUsers = responseMainUsuarios.stream()
                .map(user -> user.getIdUser())
                .toList();
        userIdsRequest.setIdUsers(idUsers);

        List<User> users = securityService.GetListUsers(userIdsRequest);
        List<ResponseMainEspecialidad1> responseEspecialidades = new ArrayList<>();

        responseMainUsuarios.forEach(usuario -> {

            if(users != null && !users.isEmpty()) {
                users.forEach(user -> {
                    if (usuario.getIdUser().equals(user.getId())) {
                        usuario.setDocumento(user.getDocumento());
                        usuario.setNombres(user.getNombres());
                        usuario.setApellidos(user.getApellidos());
                        usuario.setUsername(user.getUsername());
                        usuario.setCargoUsuario(user.getCargo());
                    }
                });
            }

            AtomicReference<Long> totalConsultasusuario = new AtomicReference<>(0L);
            usuario.setConsultas(sIJDetailConsultaIA.stream().filter(consulta ->
                    consulta.getUserId().equals(usuario.getIdUser())
            ).count());

            especialidades.forEach(especialidad -> {
                if(response.getCodInstancia().equals("0") || response.getCodInstancia().equals(especialidad.getCodigoInstancia())){
                    ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                    responseMainEspecialidad1.setCodEspecialidad(especialidad.getCodigoEspecialidad());
                    responseMainEspecialidad1.setEspecialidad(especialidad.getEspecialidad());

                    if(response.getCodInstancia().equals("0")) {
                        responseMainEspecialidad1.setConsultas(sIJDetailConsultaIA.stream().filter(consulta ->
                                consulta.getUserId().equals(usuario.getIdUser()) && (
                                consulta.getDetailConsultaIA().getSendMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase()) ||
                                consulta.getDetailConsultaIA().getResponseMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase())
                                )
                        ).count());
                    } else {
                        responseMainEspecialidad1.setConsultas(sIJDetailConsultaIA.stream().filter(consulta ->
                                consulta.getUserId().equals(usuario.getIdUser()) && (
                                consulta.getCodInstancia().equals(response.getCodInstancia()) && (
                                        consulta.getDetailConsultaIA().getSendMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase()) ||
                                        consulta.getDetailConsultaIA().getResponseMessage().toLowerCase().contains(especialidad.getEspecialidad().toLowerCase())
                                        )
                                )
                        ).count());
                    }

                    responseEspecialidades.add(responseMainEspecialidad1);
                    totalConsultasusuario.set(totalConsultasusuario.get() + responseMainEspecialidad1.getConsultas());
                }
            });

            if(usuario.getConsultas() > totalConsultasusuario.get()){
                if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
                    response.setConsultas(totalConsultas.get());
                } else {
                    ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                    responseMainEspecialidad1.setCodEspecialidad("0");
                    responseMainEspecialidad1.setEspecialidad("OTROS");
                    responseMainEspecialidad1.setConsultas(usuario.getConsultas() - totalConsultasusuario.get());

                    responseEspecialidades.add(responseMainEspecialidad1);
                }
            } else{
                if(inputData.getCodEspecialidad() == null || inputData.getCodEspecialidad().isEmpty()) {
                    ResponseMainEspecialidad1 responseMainEspecialidad1 = new ResponseMainEspecialidad1();

                    responseMainEspecialidad1.setCodEspecialidad("0");
                    responseMainEspecialidad1.setEspecialidad("OTROS");
                    responseMainEspecialidad1.setConsultas(0L);

                    responseEspecialidades.add(responseMainEspecialidad1);
                }
                usuario.setConsultas(totalConsultasusuario.get());
            }
            usuario.setEspecialidades(responseEspecialidades);

            totalConsultas.set(totalConsultas.get() + usuario.getConsultas());
        });

        response.setConsultas(totalConsultas.get());

        response.setUsuarios(responseMainUsuarios);

        return response;
    }
}
