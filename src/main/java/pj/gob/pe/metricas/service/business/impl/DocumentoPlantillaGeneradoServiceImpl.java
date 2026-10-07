package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.metricas.dao.CabDocumentoPlantillaGeneradoDAO;
import pj.gob.pe.metricas.dao.DetDocumentoPlantillaVariableDAO;
import pj.gob.pe.metricas.exception.ModeloNotFoundException;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.beans.DocumentoPlantillaGeneradoToKafka;
import pj.gob.pe.metricas.model.beans.VariableDocumentoToKafka;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;
import pj.gob.pe.metricas.service.business.DocumentoPlantillaGeneradoService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.Constantes;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.docplantilla.InputDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.docplantilla.ResponseResumenDocPlantilla;
import pj.gob.pe.metricas.utils.responses.docplantilla.ResponseResumenItemDocPlantilla;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/**
 * Métricas del flujo de generación de documentos por plantilla (tópico
 * judicial-documentos-generado-v2). Registra cabecera + variables de forma idempotente por
 * sessionUID (Kafka puede reentregar un mensaje) y expone el detalle y un resumen para reportes,
 * con la misma regla de visibilidad por usuario que DocumentoGeneradoServiceImpl.
 */
@Service
@RequiredArgsConstructor
public class DocumentoPlantillaGeneradoServiceImpl implements DocumentoPlantillaGeneradoService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoPlantillaGeneradoServiceImpl.class);

    private static final String TYPEDOC_DOC = "doc";
    private static final String TYPEDOC_WEB = "web";
    private static final String IA_EXITOSO = "EXITOSO";
    private static final String IA_ERROR = "ERROR";
    private static final String IA_NO_APLICA = "NO_APLICA";

    private final CabDocumentoPlantillaGeneradoDAO cabDocumentoPlantillaGeneradoDAO;
    private final DetDocumentoPlantillaVariableDAO detDocumentoPlantillaVariableDAO;
    private final SecurityService securityService;

    // ====================================================================
    // Registro desde Kafka
    // ====================================================================

    @Override
    @Transactional
    public void RegistrarDocumentoPlantillaGenerado(DocumentoPlantillaGeneradoToKafka evento) throws Exception {

        if (evento.getSessionUID() == null || evento.getSessionUID().isBlank()) {
            logger.warn("Documento por plantilla sin sessionUID, se descarta: {}", evento);
            return;
        }
        if (cabDocumentoPlantillaGeneradoDAO.findBySessionUID(evento.getSessionUID()) != null) {
            logger.info("Documento por plantilla sessionUID={} ya registrado, se ignora la reentrega", evento.getSessionUID());
            return;
        }

        CabDocumentoPlantillaGenerado cab = new CabDocumentoPlantillaGenerado();
        BeanUtils.copyProperties(evento, cab, "variables");
        cab.setId(null);

        if (cab.getRegDatetime() == null) {
            LocalDateTime ahora = LocalDateTime.now();
            cab.setRegDate(ahora.toLocalDate());
            cab.setRegDatetime(ahora);
            cab.setRegTimestamp(ahora.toEpochSecond(ZoneOffset.UTC));
        }

        cab = cabDocumentoPlantillaGeneradoDAO.registrar(cab);

        List<VariableDocumentoToKafka> variables = evento.getVariables() != null ? evento.getVariables() : Collections.emptyList();
        if (variables.isEmpty()) return;

        List<DetDocumentoPlantillaVariable> detalle = new ArrayList<>();
        for (VariableDocumentoToKafka v : variables) {
            DetDocumentoPlantillaVariable det = new DetDocumentoPlantillaVariable();
            det.setIdCab(cab.getId());
            det.setSessionUID(cab.getSessionUID());
            det.setUserId(cab.getUserId());
            det.setIdPlantilla(cab.getIdPlantilla());
            det.setCodigoPlantilla(cab.getCodigoPlantilla());
            det.setNombre(v.getNombre());
            det.setTipo(v.getTipo());
            det.setCampoSistema(v.getCampoSistema());
            det.setTieneValor(Boolean.TRUE.equals(v.getTieneValor()) ? Constantes.REGISTRO_ACTIVO : Constantes.REGISTRO_INACTIVO);
            det.setRegDate(cab.getRegDate());
            det.setRegDatetime(cab.getRegDatetime());
            det.setRegTimestamp(cab.getRegTimestamp());
            detalle.add(det);
        }
        detDocumentoPlantillaVariableDAO.registrarTodos(detalle);
    }

    // ====================================================================
    // Consultas
    // ====================================================================

    @Override
    public Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(String SessionId, InputDocumentoPlantillaGenerado inputData, Pageable pageable) {

        ResponseLogin responseLogin = validarSesion(SessionId);

        Map<String, Object> filters = new HashMap<>();
        if (!filtrosUsuario(responseLogin, inputData, filters)) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        filtrosDocumento(inputData, filters);

        return cabDocumentoPlantillaGeneradoDAO.getDocumentosPlantillaGenerados(filters, filtrosFecha(inputData), pageable);
    }

    @Override
    public List<DetDocumentoPlantillaVariable> getVariables(String SessionId, Long idCab) throws Exception {

        ResponseLogin responseLogin = validarSesion(SessionId);

        CabDocumentoPlantillaGenerado cab = cabDocumentoPlantillaGeneradoDAO.listarPorId(idCab);
        if (cab == null
                || (Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)
                    && !Objects.equals(cab.getUserId(), responseLogin.getUser().getIdUser()))) {
            throw new ModeloNotFoundException("Documento generado no encontrado: " + idCab);
        }

        return detDocumentoPlantillaVariableDAO.listarPorCabecera(idCab);
    }

    @Override
    public ResponseResumenDocPlantilla getResumen(String SessionId, InputDocumentoPlantillaGenerado inputData) {

        ResponseLogin responseLogin = validarSesion(SessionId);

        Map<String, Object> filters = new HashMap<>();
        List<CabDocumentoPlantillaGenerado> datos = filtrosUsuario(responseLogin, inputData, filters)
                ? cabDocumentoPlantillaGeneradoDAO.getListDocumentosPlantillaGenerados(aplicar(filters, inputData), filtrosFecha(inputData))
                : Collections.emptyList();

        List<CabDocumentoPlantillaGenerado> exitosos = datos.stream()
                .filter(d -> Constantes.COMPLETION_EXITOSO.equals(d.getStatus()))
                .toList();

        ResponseResumenDocPlantilla r = new ResponseResumenDocPlantilla();
        r.setTotalGenerados((long) datos.size());
        r.setTotalExitosos((long) exitosos.size());
        r.setTotalErrores(datos.stream().filter(d -> Constantes.COMPLETION_ERROR.equals(d.getStatus())).count());
        r.setTotalDoc(datos.stream().filter(d -> TYPEDOC_DOC.equals(d.getTypedoc())).count());
        r.setTotalWeb(datos.stream().filter(d -> TYPEDOC_WEB.equals(d.getTypedoc())).count());

        r.setIaExitoso(datos.stream().filter(d -> IA_EXITOSO.equals(d.getEstadoIA())).count());
        r.setIaError(datos.stream().filter(d -> IA_ERROR.equals(d.getEstadoIA())).count());
        r.setIaNoAplica(datos.stream().filter(d -> IA_NO_APLICA.equals(d.getEstadoIA())).count());
        r.setParrafosEnviadosIA(sumar(datos, d -> n(d.getParrafosEnviadosIA())));
        r.setParrafosCorregidosIA(sumar(datos, d -> n(d.getParrafosCorregidosIA())));
        r.setBloquesSolicitadosIA(sumar(datos, d -> n(d.getBloquesSolicitadosIA())));
        r.setBloquesGeneradosIA(sumar(datos, d -> n(d.getBloquesGeneradosIA())));
        r.setPromptTokens(sumar(datos, d -> n(d.getPromptTokens())));
        r.setCandidatesTokens(sumar(datos, d -> n(d.getCandidatesTokens())));
        r.setThoughtsTokens(sumar(datos, d -> n(d.getThoughtsTokens())));
        r.setTotalTokens(sumar(datos, d -> n(d.getTotalTokens())));

        r.setTotalVariables(sumar(datos, d -> n(d.getTotalVariables())));
        r.setVariablesSinValor(sumar(datos, d -> n(d.getVariablesSinValor())));

        r.setPromedioTiempoTotalMs(promedio(exitosos, CabDocumentoPlantillaGenerado::getTiempoTotalMs));
        r.setPromedioTiempoSijMs(promedio(exitosos, CabDocumentoPlantillaGenerado::getTiempoSijMs));
        r.setPromedioTiempoIAMs(promedio(exitosos.stream().filter(d -> d.getTiempoIAMs() != null).toList(),
                CabDocumentoPlantillaGenerado::getTiempoIAMs));

        r.setPorDocumento(agrupar(datos, d -> String.valueOf(d.getIdDocumento()), CabDocumentoPlantillaGenerado::getDocumento));
        r.setPorPlantilla(agrupar(datos, d -> d.getCodigoPlantilla() + " v" + d.getVersionPlantilla(), CabDocumentoPlantillaGenerado::getNombreOutPlantilla));
        r.setPorInstancia(agrupar(datos, CabDocumentoPlantillaGenerado::getCodInstancia, CabDocumentoPlantillaGenerado::getInstancia));
        r.setPorUsuario(agrupar(datos, d -> String.valueOf(d.getUserId()), CabDocumentoPlantillaGenerado::getNombreUsuario));

        return r;
    }

    // ====================================================================
    // Soporte
    // ====================================================================

    private Map<String, Object> aplicar(Map<String, Object> filters, InputDocumentoPlantillaGenerado inputData) {
        filtrosDocumento(inputData, filters);
        return filters;
    }

    /**
     * Visibilidad por usuario (misma regla que DocumentoGeneradoServiceImpl): el usuario normal solo
     * ve lo suyo; el resto puede filtrar por un usuario o buscar usuarios por sus datos.
     *
     * @return false si la búsqueda de usuarios no encontró a nadie (el resultado es vacío)
     */
    private boolean filtrosUsuario(ResponseLogin responseLogin, InputDocumentoPlantillaGenerado inputData, Map<String, Object> filters) {

        if (Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)) {
            filters.put("userId", responseLogin.getUser().getIdUser());
            return true;
        }
        if (inputData.getIdUser() != null && inputData.getIdUser() > 0) {
            filters.put("userId", inputData.getIdUser());
            return true;
        }

        InputConsultaIAExternal busqueda = new InputConsultaIAExternal();
        boolean buscarUsuarios = false;

        if (noVacio(inputData.getDocumento())) { busqueda.setDocumento(inputData.getDocumento()); buscarUsuarios = true; }
        if (noVacio(inputData.getNombres())) { busqueda.setNombres(inputData.getNombres()); buscarUsuarios = true; }
        if (noVacio(inputData.getApellidos())) { busqueda.setApellidos(inputData.getApellidos()); buscarUsuarios = true; }
        if (noVacio(inputData.getCargo())) { busqueda.setCargo(inputData.getCargo()); buscarUsuarios = true; }
        if (noVacio(inputData.getUsername())) { busqueda.setUsername(inputData.getUsername()); buscarUsuarios = true; }
        if (noVacio(inputData.getEmail())) { busqueda.setEmail(inputData.getEmail()); buscarUsuarios = true; }

        if (!buscarUsuarios) return true;

        List<OutputConsultaIAExternal> usuarios = securityService.Getusers(busqueda);
        if (usuarios == null || usuarios.isEmpty()) return false;

        filters.put("list_userId", usuarios.stream().map(OutputConsultaIAExternal::getId).toList());
        return true;
    }

    private void filtrosDocumento(InputDocumentoPlantillaGenerado in, Map<String, Object> filters) {
        if (noVacio(in.getTypedoc())) filters.put("typedoc", in.getTypedoc());
        if (in.getStatus() != null) filters.put("status", in.getStatus());
        if (noVacio(in.getEstadoIA())) filters.put("estadoIA", in.getEstadoIA());
        if (noVacio(in.getCodSede())) filters.put("codSede", in.getCodSede());
        if (noVacio(in.getCodInstancia())) filters.put("codInstancia", in.getCodInstancia());
        if (noVacio(in.getCodEspecialidad())) filters.put("codEspecialidad", in.getCodEspecialidad());
        if (noVacio(in.getCodMateria())) filters.put("codMateria", in.getCodMateria());
        if (noVacio(in.getNumeroExpediente())) {
            String numero = in.getNumeroExpediente().trim();
            filters.put("codNumero", "0".repeat(Math.max(0, 5 - numero.length())) + numero);
        }
        if (noVacio(in.getYearExpediente())) filters.put("codYear", in.getYearExpediente());
        if (in.getNumUnico() != null && in.getNumUnico() > 0) filters.put("nUnico", in.getNumUnico());
        if (noVacio(in.getXdeFormato())) filters.put("xFormato", in.getXdeFormato());
        if (noVacio(in.getDniDemandante())) filters.put("dniDemandante", in.getDniDemandante());
        if (noVacio(in.getDniDemandado())) filters.put("dniDemandado", in.getDniDemandado());
        if (in.getIdTipoDocumento() != null && in.getIdTipoDocumento() > 0) filters.put("idTipoDocumento", in.getIdTipoDocumento());
        if (in.getIdDocumento() != null && in.getIdDocumento() > 0) filters.put("idDocumento", in.getIdDocumento());
        if (in.getIdPlantilla() != null && in.getIdPlantilla() > 0) filters.put("idPlantilla", in.getIdPlantilla());
        if (noVacio(in.getCodigoPlantilla())) filters.put("codigoPlantilla", in.getCodigoPlantilla());
        if (in.getVersionPlantilla() != null && in.getVersionPlantilla() > 0) filters.put("versionPlantilla", in.getVersionPlantilla());
        if (noVacio(in.getModel())) filters.put("model", in.getModel());
    }

    private Map<String, Object> filtrosFecha(InputDocumentoPlantillaGenerado in) {
        Map<String, Object> filtersFecha = new HashMap<>();
        if (in.getFechaInicio() != null) filtersFecha.put("fechaInicio", in.getFechaInicio());
        if (in.getFechaFin() != null) filtersFecha.put("fechaFin", in.getFechaFin());
        return filtersFecha;
    }

    private List<ResponseResumenItemDocPlantilla> agrupar(List<CabDocumentoPlantillaGenerado> datos,
                                                         Function<CabDocumentoPlantillaGenerado, String> clave,
                                                         Function<CabDocumentoPlantillaGenerado, String> descripcion) {

        Map<String, List<CabDocumentoPlantillaGenerado>> grupos = new LinkedHashMap<>();
        for (CabDocumentoPlantillaGenerado d : datos) {
            grupos.computeIfAbsent(String.valueOf(clave.apply(d)), k -> new ArrayList<>()).add(d);
        }

        List<ResponseResumenItemDocPlantilla> items = new ArrayList<>();
        grupos.forEach((codigo, grupo) -> {
            List<CabDocumentoPlantillaGenerado> exitosos = grupo.stream()
                    .filter(d -> Constantes.COMPLETION_EXITOSO.equals(d.getStatus())).toList();

            items.add(new ResponseResumenItemDocPlantilla(
                    codigo,
                    grupo.stream().map(descripcion).filter(Objects::nonNull).findFirst().orElse(null),
                    (long) grupo.size(),
                    (long) exitosos.size(),
                    grupo.stream().filter(d -> Constantes.COMPLETION_ERROR.equals(d.getStatus())).count(),
                    grupo.stream().filter(d -> TYPEDOC_DOC.equals(d.getTypedoc())).count(),
                    grupo.stream().filter(d -> TYPEDOC_WEB.equals(d.getTypedoc())).count(),
                    sumar(grupo, d -> n(d.getTotalTokens())),
                    sumar(grupo, d -> n(d.getVariablesSinValor())),
                    promedio(exitosos, CabDocumentoPlantillaGenerado::getTiempoTotalMs)));
        });

        items.sort(Comparator.comparing(ResponseResumenItemDocPlantilla::getTotal).reversed());
        return items;
    }

    private static long sumar(List<CabDocumentoPlantillaGenerado> datos, ToLongFunction<CabDocumentoPlantillaGenerado> valor) {
        return datos.stream().mapToLong(valor).sum();
    }

    private static Double promedio(List<CabDocumentoPlantillaGenerado> datos, Function<CabDocumentoPlantillaGenerado, Long> valor) {
        return datos.stream().map(valor).filter(Objects::nonNull).mapToLong(Long::longValue).average().orElse(0.0);
    }

    private static long n(Integer valor) {
        return valor == null ? 0L : valor;
    }

    private static boolean noVacio(String valor) {
        return valor != null && !valor.isBlank();
    }

    private ResponseLogin validarSesion(String SessionId) {

        if (SessionId == null || SessionId.isEmpty()) {
            throw new ValidationSessionServiceException("La sessión remitida es inválida");
        }

        ResponseLogin responseLogin = securityService.GetSessionData(SessionId);

        if (responseLogin == null || !responseLogin.isSuccess() || !responseLogin.isItemFound() || responseLogin.getUser() == null) {
            throw new ValidationSessionServiceException("La sessión remitida es inválida");
        }

        return responseLogin;
    }
}
