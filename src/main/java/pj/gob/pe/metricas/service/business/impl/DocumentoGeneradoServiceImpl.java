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
import pj.gob.pe.metricas.service.business.DocumentoGeneradoService;
import pj.gob.pe.metricas.service.externals.JudicialService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.*;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputMainDocGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.docgenerados.*;
import pj.gob.pe.metricas.utils.responses.judicial.DataEspecialidadDTO;
import pj.gob.pe.metricas.utils.responses.judicial.DataInstanciaDTO;
import pj.gob.pe.metricas.utils.responses.judicial.Documento;
import pj.gob.pe.metricas.utils.responses.judicial.TipoDocumento;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentoGeneradoServiceImpl implements DocumentoGeneradoService {

    private final SecurityService securityService;
    private final JudicialService judicialService;
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
        if(inputData.getNumUnico() != null && inputData.getNumUnico() > 0) {
            filters.put("nUnico", inputData.getNumUnico());
        }
        if(inputData.getXdeFormato() != null && !inputData.getXdeFormato().isEmpty()) {
            filters.put("xFormato", inputData.getXdeFormato());
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

    @Override
    public Long getTotalDocGenerados(String buscar, String SessionId) throws Exception {

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
        filters.put("userId", responseLogin.getUser().getIdUser());

        Map<String, Object> filtersNotEquals = new HashMap<>();

        Long totalElementos = cabDocumentoGeneradoDAO.getTotalDocGenerados(filters, filtersNotEquals);

        return totalElementos;
    }

    @Override
    public ResponseTotalFiltersDocGenerados getTotalDocGeneradosFilters(InputTotalesCabDocGenerado inputData, String SessionId) throws Exception {

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

        ResponseTotalFiltersDocGenerados responseTotalFiltersDocGenerados = new ResponseTotalFiltersDocGenerados();
        boolean pasaValidacionUsuarios = true;

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
                        pasaValidacionUsuarios = false;
                    }

                    // Agregar los IDs de los usuarios encontrados
                    filters.put("list_userId", listUsers.stream().map(OutputConsultaIAExternal::getId).toList());
                }
            }
        }

        if(inputData.getTypedoc() != null && !inputData.getTypedoc().isEmpty()) {
            filters.put("typedoc", inputData.getTypedoc());
            responseTotalFiltersDocGenerados.setTypedoc(inputData.getTypedoc());
        }
        if(inputData.getCodSede() != null && !inputData.getCodSede().isEmpty()) {
            filters.put("codSede", inputData.getCodSede());
            responseTotalFiltersDocGenerados.setCodSede(inputData.getCodSede());
        }
        if(inputData.getCodInstancia() != null && !inputData.getCodInstancia().isEmpty()) {
            filters.put("codInstancia", inputData.getCodInstancia());
            responseTotalFiltersDocGenerados.setCodInstancia(inputData.getCodInstancia());
        }
        if(inputData.getCodEspecialidad() != null && !inputData.getCodEspecialidad().isEmpty()) {
            filters.put("codEspecialidad", inputData.getCodEspecialidad());
            responseTotalFiltersDocGenerados.setCodEspecialidad(inputData.getCodEspecialidad());
        }
        if(inputData.getCodMateria() != null && !inputData.getCodMateria().isEmpty()) {
            filters.put("codMateria", inputData.getCodMateria());
            responseTotalFiltersDocGenerados.setCodMateria(inputData.getCodMateria());
        }
        if(inputData.getNumeroExpediente() != null && !inputData.getNumeroExpediente().isEmpty()) {
            filters.put("codNumero", "0".repeat(5 - inputData.getNumeroExpediente().length()) + inputData.getNumeroExpediente());
            responseTotalFiltersDocGenerados.setNumeroExpediente(inputData.getNumeroExpediente());
        }
        if(inputData.getYearExpediente() != null && !inputData.getYearExpediente().isEmpty()) {
            filters.put("codYear", inputData.getYearExpediente());
            responseTotalFiltersDocGenerados.setYearExpediente(inputData.getYearExpediente());
        }
        if(inputData.getIdDocumento() != null && inputData.getIdDocumento() > 0) {
            filters.put("idDocumento", inputData.getIdDocumento());
            responseTotalFiltersDocGenerados.setIdDocumento(inputData.getIdDocumento());
        }
        if(inputData.getIdTipoDocumento() != null && inputData.getIdTipoDocumento() > 0) {
            filters.put("idTipoDocumento", inputData.getIdTipoDocumento());
            responseTotalFiltersDocGenerados.setIdTipoDocumento(inputData.getIdTipoDocumento());
        }
        if(inputData.getNumUnico() != null && inputData.getNumUnico() > 0) {
            filters.put("nUnico", inputData.getNumUnico());
            responseTotalFiltersDocGenerados.setNumUnico(inputData.getNumUnico());
        }
        if(inputData.getXdeFormato() != null && !inputData.getXdeFormato().isEmpty()) {
            filters.put("xFormato", inputData.getXdeFormato());
            responseTotalFiltersDocGenerados.setXdeFormato(inputData.getXdeFormato());
        }
        if(inputData.getDniDemandante() != null && !inputData.getDniDemandante().isEmpty()) {
            filters.put("dniDemandante", inputData.getDniDemandante());
            responseTotalFiltersDocGenerados.setDniDemandante(inputData.getDniDemandante());
        }
        if(inputData.getDniDemandado() != null && !inputData.getDniDemandado().isEmpty()) {
            filters.put("dniDemandado", inputData.getDniDemandado());
            responseTotalFiltersDocGenerados.setDniDemandado(inputData.getDniDemandado());
        }
        if(inputData.getTemplateCode() != null && !inputData.getTemplateCode().isEmpty()) {
            filters.put("templateCode", inputData.getTemplateCode());
            responseTotalFiltersDocGenerados.setTemplateCode(inputData.getTemplateCode());
        }
        if(inputData.getTemplateID() != null && inputData.getTemplateID() > 0) {
            filters.put("templateID", inputData.getTemplateID());
            responseTotalFiltersDocGenerados.setTemplateID(inputData.getTemplateID());
        }

        if(inputData.getUbicacion() != null && !inputData.getUbicacion().isEmpty()) {
            filters.put("ubicacion", inputData.getUbicacion());
            responseTotalFiltersDocGenerados.setUbicacion(inputData.getUbicacion());
        }
        if(inputData.getJuez() != null && !inputData.getJuez().isEmpty()) {
            filters.put("juez", inputData.getJuez());
            responseTotalFiltersDocGenerados.setJuez(inputData.getJuez());
        }
        if(inputData.getEstado() != null && !inputData.getEstado().isEmpty()) {
            filters.put("estado", inputData.getEstado());
            responseTotalFiltersDocGenerados.setEstado(inputData.getEstado());
        }
        if(inputData.getModel() != null && !inputData.getModel().isEmpty()) {
            filters.put("model", inputData.getModel());
            responseTotalFiltersDocGenerados.setModel(inputData.getModel());
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

        if(!pasaValidacionUsuarios){
            responseTotalFiltersDocGenerados.setTotalDocsGenerados(0L);
            return responseTotalFiltersDocGenerados;
        }

        List<CabDocumentoGenerado> response = cabDocumentoGeneradoDAO.getListGeneralDocumentoGeneradoIA(filters, filtersNotEquals, filtersFecha);

        responseTotalFiltersDocGenerados.setTotalDocsGenerados(response.stream().count());

        if(responseTotalFiltersDocGenerados.getTotalDocsGenerados() > 0L){
            if(responseTotalFiltersDocGenerados.getCodSede()  != null)
                responseTotalFiltersDocGenerados.setSede(response.getFirst().getSede());

            if(responseTotalFiltersDocGenerados.getCodInstancia()  != null)
                responseTotalFiltersDocGenerados.setInstancia(response.getFirst().getInstancia());

            if(responseTotalFiltersDocGenerados.getCodEspecialidad()  != null)
                responseTotalFiltersDocGenerados.setEspecialidad(response.getFirst().getEspecialidad());

            if(responseTotalFiltersDocGenerados.getCodMateria()  != null)
                responseTotalFiltersDocGenerados.setMateria(response.getFirst().getMateria());

            if(responseTotalFiltersDocGenerados.getIdDocumento()  != null)
                responseTotalFiltersDocGenerados.setDocumento(response.getFirst().getDocumento());

            if(responseTotalFiltersDocGenerados.getIdTipoDocumento()  != null)
                responseTotalFiltersDocGenerados.setTipoDocumento(response.getFirst().getTipoDocumento());

            if(responseTotalFiltersDocGenerados.getTemplateID()  != null || responseTotalFiltersDocGenerados.getTemplateCode()  != null)
                responseTotalFiltersDocGenerados.setTemplateNombreOut(response.getFirst().getTemplateNombreOut());
        }

        return responseTotalFiltersDocGenerados;
    }

    @Override
    public ResponseMainSumarisimo getMainDocumentoGenerado(InputMainDocGenerado inputData, String SessionId) throws Exception {
        ResponseMainSumarisimo response = new ResponseMainSumarisimo();
        response.setTotalDoc(0L);
        response.setTotalWeb(0L);

        List<DataInstanciaDTO> instancias = judicialService.GetInstancias();
        List<DataEspecialidadDTO> especialidades = judicialService.GetEspecialidades();
        List<TipoDocumento> tipoDocumentos = judicialService.GetTipoDocumento();
        List<Documento> documentos = judicialService.GetDocumento();

        if(inputData.getJuez() != null && !inputData.getJuez().isEmpty())
            response.setJuez(inputData.getJuez());
        else
            response.setJuez("Todos");

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
        if(inputData.getJuez() != null && !inputData.getJuez().isEmpty()) {
            filters.put("juez", inputData.getJuez());
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
        List<CabDocumentoGenerado> responseCabDocumentoGenerado = cabDocumentoGeneradoDAO.getListGeneralDocumentoGeneradoIA(filters, filtersNotEquals, filtersFecha);

        response.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc -> doc.getTypedoc().equals("doc")).count());
        response.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc -> doc.getTypedoc().equals("web")).count());

        List<ResponseMainEspecialidad> responseEspecialidades = new ArrayList<>();

        especialidades.forEach(especialidad -> {
            if(response.getCodInstancia().equals("0") || response.getCodInstancia().equals(especialidad.getCodigoInstancia())){
                ResponseMainEspecialidad responseMainEspecialidad = new ResponseMainEspecialidad();

                responseMainEspecialidad.setCodEspecialidad(especialidad.getCodigoEspecialidad());
                responseMainEspecialidad.setEspecialidad(especialidad.getEspecialidad());

                if(response.getCodInstancia().equals("0")){
                    responseMainEspecialidad.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                       doc.getTypedoc().equals("doc")
                                    && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())).count());

                    responseMainEspecialidad.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                       doc.getTypedoc().equals("web")
                                    && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())).count());
                } else {
                    responseMainEspecialidad.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                       doc.getTypedoc().equals("doc")
                                    && doc.getCodInstancia().equals(response.getCodInstancia())
                                    && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())).count());

                    responseMainEspecialidad.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                       doc.getTypedoc().equals("web")
                                    && doc.getCodInstancia().equals(response.getCodInstancia())
                                    && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())).count());
                }

                List<ResponseMainTipoDoc> responseMainTipoDocsInicial = new ArrayList<>();

                tipoDocumentos.forEach(tipoDocumento -> {
                    if(response.getCodInstancia().equals("0")){
                        ResponseMainTipoDoc responseMainTipoDoc = new ResponseMainTipoDoc();
                        responseMainTipoDoc.setIdTipoDocumento(0L);
                        responseMainTipoDoc.setTipoDocumento(tipoDocumento.getDescripcion());

                        responseMainTipoDocsInicial.add(responseMainTipoDoc);
                    } else {
                        if(response.getCodInstancia().equals(tipoDocumento.getIdInstancia())){
                            ResponseMainTipoDoc responseMainTipoDoc = new ResponseMainTipoDoc();
                            responseMainTipoDoc.setIdTipoDocumento(tipoDocumento.getIdTipoDocumento());
                            responseMainTipoDoc.setTipoDocumento(tipoDocumento.getDescripcion());

                            responseMainTipoDocsInicial.add(responseMainTipoDoc);
                        }
                    }
                });

                List<ResponseMainTipoDoc> responseMainTipoDocs = responseMainTipoDocsInicial.stream()
                        .distinct()
                        .collect(Collectors.toList());

                responseMainTipoDocs.forEach(responseMainTipoDoc -> {

                    if(response.getCodInstancia().equals("0")){
                        responseMainTipoDoc.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                doc.getTypedoc().equals("doc")
                                        && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                        && doc.getTipoDocumento().equals(responseMainTipoDoc.getTipoDocumento())).count());

                        responseMainTipoDoc.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                doc.getTypedoc().equals("web")
                                        && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                        && doc.getTipoDocumento().equals(responseMainTipoDoc.getTipoDocumento())).count());
                    } else {
                        responseMainTipoDoc.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                           doc.getTypedoc().equals("doc")
                                        && doc.getCodInstancia().equals(response.getCodInstancia())
                                        && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                        && doc.getIdTipoDocumento().equals(responseMainTipoDoc.getIdTipoDocumento())).count());

                        responseMainTipoDoc.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                           doc.getTypedoc().equals("web")
                                        && doc.getCodInstancia().equals(response.getCodInstancia())
                                        && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                        && doc.getIdTipoDocumento().equals(responseMainTipoDoc.getIdTipoDocumento())).count());
                    }

                    List<ResponseMainDoc> responseMainDocsInicial = new ArrayList<>();

                    documentos.forEach(documento -> {
                        if(response.getCodInstancia().equals("0")){
                            tipoDocumentos.forEach(tipoDocumento -> {
                                if(tipoDocumento.getDescripcion().equals(responseMainTipoDoc.getTipoDocumento())
                                && tipoDocumento.getIdTipoDocumento().equals(documento.getIdTipoDocumento())) {
                                    ResponseMainDoc responseMainDoc = new ResponseMainDoc();
                                    responseMainDoc.setIdDocumento(0L);
                                    responseMainDoc.setDocumento(documento.getDescripcion());

                                    responseMainDocsInicial.add(responseMainDoc);
                                }

                            });
                        } else {
                            if(responseMainTipoDoc.getIdTipoDocumento().equals(documento.getIdTipoDocumento())){
                                ResponseMainDoc responseMainDoc = new ResponseMainDoc();
                                responseMainDoc.setIdDocumento(documento.getIdDocumento());
                                responseMainDoc.setDocumento(documento.getDescripcion());

                                responseMainDocsInicial.add(responseMainDoc);
                            }
                        }
                    });

                    List<ResponseMainDoc> responseMainDocs = responseMainDocsInicial.stream()
                            .distinct()
                            .collect(Collectors.toList());

                    responseMainDocs.forEach(responseMainDoc -> {

                        if(response.getCodInstancia().equals("0")){
                            responseMainDoc.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                    doc.getTypedoc().equals("doc")
                                            && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                            && doc.getTipoDocumento().equals(responseMainTipoDoc.getTipoDocumento())
                                            && doc.getDocumento().equals(responseMainDoc.getDocumento())).count());

                            responseMainDoc.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                    doc.getTypedoc().equals("web")
                                            && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                            && doc.getTipoDocumento().equals(responseMainTipoDoc.getTipoDocumento())
                                            && doc.getDocumento().equals(responseMainDoc.getDocumento())).count());
                        } else {
                            responseMainDoc.setTotalDoc(responseCabDocumentoGenerado.stream().filter(doc ->
                                    doc.getTypedoc().equals("doc")
                                            && doc.getCodInstancia().equals(response.getCodInstancia())
                                            && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                            && doc.getIdTipoDocumento().equals(responseMainTipoDoc.getIdTipoDocumento())
                                            && doc.getIdDocumento().equals(responseMainDoc.getIdDocumento())).count());

                            responseMainDoc.setTotalWeb(responseCabDocumentoGenerado.stream().filter(doc ->
                                    doc.getTypedoc().equals("web")
                                            && doc.getCodInstancia().equals(response.getCodInstancia())
                                            && doc.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad())
                                            && doc.getIdTipoDocumento().equals(responseMainTipoDoc.getIdTipoDocumento())
                                            && doc.getIdDocumento().equals(responseMainDoc.getIdDocumento())).count());
                            }
                        });

                        responseMainTipoDoc.setDocumentos(responseMainDocs);
                    });

                responseMainEspecialidad.setTipoDocumentos(responseMainTipoDocs);
                responseEspecialidades.add(responseMainEspecialidad);
            }

        });

        response.setEspecialidades(responseEspecialidades);


        return response;
    }
}
