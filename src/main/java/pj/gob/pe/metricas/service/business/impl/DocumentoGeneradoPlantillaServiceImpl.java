package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.dao.CabDocumentoPlantillaGeneradoDAO;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoPlantillaService;
import pj.gob.pe.metricas.service.externals.JudicialService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.Constantes;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputMainDocGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainDoc;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainEspecialidad;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainTipoDoc;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.*;
import pj.gob.pe.metricas.utils.responses.judicial.DataEspecialidadDTO;
import pj.gob.pe.metricas.utils.responses.judicial.DataInstanciaDTO;
import pj.gob.pe.metricas.utils.responses.judicial.Documento;
import pj.gob.pe.metricas.utils.responses.judicial.TipoDocumento;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Misma lógica que {@link DocumentoGeneradoServiceImpl}, pero sobre CabDocumentoPlantillaGenerado.
 * <p>
 * La tabla del flujo por plantilla registra también las generaciones fallidas: los totales y el
 * resumen main cuentan solo las exitosas (status 1), equivalentes a las filas de la tabla
 * anterior; las fallidas se informan aparte en {@code totalErrores}. El listado get-data devuelve
 * todas las filas con su {@code status}.
 */
@Service
@RequiredArgsConstructor
public class DocumentoGeneradoPlantillaServiceImpl implements DocumentoGeneradoPlantillaService {

    private static final String TYPEDOC_DOC = "doc";
    private static final String TYPEDOC_WEB = "web";
    private static final String IA_EXITOSO = "EXITOSO";
    private static final String IA_ERROR = "ERROR";
    private static final String IA_NO_APLICA = "NO_APLICA";

    private final SecurityService securityService;
    private final JudicialService judicialService;
    private final CabDocumentoPlantillaGeneradoDAO cabDocumentoPlantillaGeneradoDAO;

    @Override
    public Page<ResponseDocumentoGeneradoPlantilla> getDocumentosGenerados(
            String SessionId,
            InputDocumentoGeneradoIA inputData,
            Pageable pageable) {

        ResponseLogin responseLogin = validarSesion(SessionId);

        Map<String, Object> filters = new HashMap<>();
        if (!filtrosUsuario(responseLogin, inputData.getIdUser(), inputData.getDocumento(), inputData.getNombres(),
                inputData.getApellidos(), inputData.getCargo(), inputData.getUsername(), inputData.getEmail(), filters)) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        if (noVacio(inputData.getTypedoc())) filters.put("typedoc", inputData.getTypedoc());
        if (noVacio(inputData.getCodSede())) filters.put("codSede", inputData.getCodSede());
        if (noVacio(inputData.getCodInstancia())) filters.put("codInstancia", inputData.getCodInstancia());
        if (noVacio(inputData.getCodEspecialidad())) filters.put("codEspecialidad", inputData.getCodEspecialidad());
        if (noVacio(inputData.getCodMateria())) filters.put("codMateria", inputData.getCodMateria());
        if (noVacio(inputData.getNumeroExpediente())) filters.put("codNumero", numeroExpediente(inputData.getNumeroExpediente()));
        if (noVacio(inputData.getYearExpediente())) filters.put("codYear", inputData.getYearExpediente());
        if (positivo(inputData.getIdDocumento())) filters.put("idDocumento", inputData.getIdDocumento());
        if (positivo(inputData.getIdTipoDocumento())) filters.put("idTipoDocumento", inputData.getIdTipoDocumento());
        if (positivo(inputData.getNumUnico())) filters.put("nUnico", inputData.getNumUnico());
        if (noVacio(inputData.getXdeFormato())) filters.put("xFormato", inputData.getXdeFormato());
        if (noVacio(inputData.getDniDemandante())) filters.put("dniDemandante", inputData.getDniDemandante());
        if (noVacio(inputData.getDniDemandado())) filters.put("dniDemandado", inputData.getDniDemandado());
        if (noVacio(inputData.getTemplateCode())) filters.put("codigoPlantilla", inputData.getTemplateCode());
        if (positivo(inputData.getTemplateID())) filters.put("idPlantilla", inputData.getTemplateID());

        return cabDocumentoPlantillaGeneradoDAO
                .getDocumentosPlantillaGenerados(filters, filtrosFecha(inputData.getFechaInicio(), inputData.getFechaFin()), pageable)
                .map(ResponseDocumentoGeneradoPlantilla::from);
    }

    @Override
    public ResponseTotalDocGeneradosPlantilla getTotalDocGenerados(String SessionId) throws Exception {

        ResponseLogin responseLogin = validarSesion(SessionId);

        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", responseLogin.getUser().getIdUser());

        List<CabDocumentoPlantillaGenerado> todos = cabDocumentoPlantillaGeneradoDAO.getListDocumentosPlantillaGenerados(filters, new HashMap<>());
        List<CabDocumentoPlantillaGenerado> exitosos = exitosos(todos);

        ResponseTotalDocGeneradosPlantilla response = new ResponseTotalDocGeneradosPlantilla();
        response.setTotalDocsGenerados((long) exitosos.size());
        llenarExtras(response, todos, exitosos);
        return response;
    }

    @Override
    public ResponseTotalFiltersDocGeneradosPlantilla getTotalDocGeneradosFilters(InputTotalesCabDocGenerado inputData, String SessionId) throws Exception {

        ResponseLogin responseLogin = validarSesion(SessionId);

        ResponseTotalFiltersDocGeneradosPlantilla response = new ResponseTotalFiltersDocGeneradosPlantilla();

        Map<String, Object> filters = new HashMap<>();
        boolean pasaValidacionUsuarios = filtrosUsuario(responseLogin, inputData.getIdUser(), inputData.getDocumento(),
                inputData.getNombres(), inputData.getApellidos(), inputData.getCargo(), inputData.getUsername(),
                inputData.getEmail(), filters);

        if (noVacio(inputData.getTypedoc())) {
            filters.put("typedoc", inputData.getTypedoc());
            response.setTypedoc(inputData.getTypedoc());
        }
        if (noVacio(inputData.getCodSede())) {
            filters.put("codSede", inputData.getCodSede());
            response.setCodSede(inputData.getCodSede());
        }
        if (noVacio(inputData.getCodInstancia())) {
            filters.put("codInstancia", inputData.getCodInstancia());
            response.setCodInstancia(inputData.getCodInstancia());
        }
        if (noVacio(inputData.getCodEspecialidad())) {
            filters.put("codEspecialidad", inputData.getCodEspecialidad());
            response.setCodEspecialidad(inputData.getCodEspecialidad());
        }
        if (noVacio(inputData.getCodMateria())) {
            filters.put("codMateria", inputData.getCodMateria());
            response.setCodMateria(inputData.getCodMateria());
        }
        if (noVacio(inputData.getNumeroExpediente())) {
            filters.put("codNumero", numeroExpediente(inputData.getNumeroExpediente()));
            response.setNumeroExpediente(inputData.getNumeroExpediente());
        }
        if (noVacio(inputData.getYearExpediente())) {
            filters.put("codYear", inputData.getYearExpediente());
            response.setYearExpediente(inputData.getYearExpediente());
        }
        if (positivo(inputData.getIdDocumento())) {
            filters.put("idDocumento", inputData.getIdDocumento());
            response.setIdDocumento(inputData.getIdDocumento());
        }
        if (positivo(inputData.getIdTipoDocumento())) {
            filters.put("idTipoDocumento", inputData.getIdTipoDocumento());
            response.setIdTipoDocumento(inputData.getIdTipoDocumento());
        }
        if (positivo(inputData.getNumUnico())) {
            filters.put("nUnico", inputData.getNumUnico());
            response.setNumUnico(inputData.getNumUnico());
        }
        if (noVacio(inputData.getXdeFormato())) {
            filters.put("xFormato", inputData.getXdeFormato());
            response.setXdeFormato(inputData.getXdeFormato());
        }
        if (noVacio(inputData.getDniDemandante())) {
            filters.put("dniDemandante", inputData.getDniDemandante());
            response.setDniDemandante(inputData.getDniDemandante());
        }
        if (noVacio(inputData.getDniDemandado())) {
            filters.put("dniDemandado", inputData.getDniDemandado());
            response.setDniDemandado(inputData.getDniDemandado());
        }
        if (noVacio(inputData.getTemplateCode())) {
            filters.put("codigoPlantilla", inputData.getTemplateCode());
            response.setTemplateCode(inputData.getTemplateCode());
        }
        if (positivo(inputData.getTemplateID())) {
            filters.put("idPlantilla", inputData.getTemplateID());
            response.setTemplateID(inputData.getTemplateID());
        }
        if (noVacio(inputData.getUbicacion())) {
            filters.put("ubicacion", inputData.getUbicacion());
            response.setUbicacion(inputData.getUbicacion());
        }
        if (noVacio(inputData.getJuez())) {
            filters.put("juez", inputData.getJuez());
            response.setJuez(inputData.getJuez());
        }
        if (noVacio(inputData.getEstado())) {
            filters.put("estado", inputData.getEstado());
            response.setEstado(inputData.getEstado());
        }
        if (noVacio(inputData.getModel())) {
            filters.put("model", inputData.getModel());
            response.setModel(inputData.getModel());
        }

        if (!pasaValidacionUsuarios) {
            response.setTotalDocsGenerados(0L);
            llenarExtras(response, Collections.emptyList(), Collections.emptyList());
            return response;
        }

        List<CabDocumentoPlantillaGenerado> todos = cabDocumentoPlantillaGeneradoDAO.getListDocumentosPlantillaGenerados(
                filters, filtrosFecha(inputData.getFechaInicio(), inputData.getFechaFin()));
        List<CabDocumentoPlantillaGenerado> exitosos = exitosos(todos);

        response.setTotalDocsGenerados((long) exitosos.size());
        llenarExtras(response, todos, exitosos);

        response.setTotalDoc(contar(exitosos, d -> TYPEDOC_DOC.equals(d.getTypedoc())));
        response.setTotalWeb(contar(exitosos, d -> TYPEDOC_WEB.equals(d.getTypedoc())));
        response.setIaNoAplica(contar(exitosos, d -> IA_NO_APLICA.equals(d.getEstadoIA())));
        response.setVariablesSinValor(exitosos.stream().mapToLong(d -> n(d.getVariablesSinValor())).sum());
        response.setFechaPrimeraGeneracion(exitosos.stream().map(CabDocumentoPlantillaGenerado::getRegDatetime)
                .filter(Objects::nonNull).min(LocalDateTime::compareTo).orElse(null));
        response.setFechaUltimaGeneracion(exitosos.stream().map(CabDocumentoPlantillaGenerado::getRegDatetime)
                .filter(Objects::nonNull).max(LocalDateTime::compareTo).orElse(null));

        if (!exitosos.isEmpty()) {
            CabDocumentoPlantillaGenerado primero = exitosos.getFirst();

            if (response.getCodSede() != null) response.setSede(primero.getSede());
            if (response.getCodInstancia() != null) response.setInstancia(primero.getInstancia());
            if (response.getCodEspecialidad() != null) response.setEspecialidad(primero.getEspecialidad());
            if (response.getCodMateria() != null) response.setMateria(primero.getMateria());
            if (response.getIdDocumento() != null) response.setDocumento(primero.getDocumento());
            if (response.getIdTipoDocumento() != null) response.setTipoDocumento(primero.getTipoDocumento());
            if (response.getTemplateID() != null || response.getTemplateCode() != null)
                response.setTemplateNombreOut(primero.getNombreOutPlantilla());
        }

        return response;
    }

    @Override
    public ResponseMainSumarisimoPlantilla getMainDocumentoGenerado(InputMainDocGenerado inputData, String SessionId) throws Exception {
        ResponseMainSumarisimoPlantilla response = new ResponseMainSumarisimoPlantilla();
        response.setTotalDoc(0L);
        response.setTotalWeb(0L);

        List<DataInstanciaDTO> instancias = judicialService.GetInstancias();
        List<DataEspecialidadDTO> especialidadesAll = judicialService.GetEspecialidades();
        List<TipoDocumento> tipoDocumentosAll = judicialService.GetTipoDocumento();
        List<Documento> documentosAll = judicialService.GetDocumento();

        List<DataEspecialidadDTO> especialidades = new ArrayList<>();
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        List<Documento> documentos = new ArrayList<>();

        if (noVacio(inputData.getJuez()))
            response.setJuez(inputData.getJuez());
        else
            response.setJuez("Todos");

        if (noVacio(inputData.getCodInstancia())) {
            response.setCodInstancia(inputData.getCodInstancia());
            response.setInstancia(instancias.stream()
                    .filter(instancia -> Objects.equals(instancia.getCodigoInstancia(), inputData.getCodInstancia()))
                    .map(DataInstanciaDTO::getInstancia)
                    .findFirst()
                    .orElse(""));
        } else {
            response.setCodInstancia("0");
            response.setInstancia("Todos");
        }

        Map<String, Object> filters = new HashMap<>();
        if (noVacio(inputData.getCodInstancia())) {
            filters.put("codInstancia", inputData.getCodInstancia());
        }
        if (noVacio(inputData.getJuez())) {
            filters.put("juez", inputData.getJuez());
        }
        if (noVacio(inputData.getCodEspecialidad())) {
            filters.put("codEspecialidad", inputData.getCodEspecialidad());
            especialidadesAll.stream()
                    .filter(especialidad -> inputData.getCodEspecialidad().equals(especialidad.getCodigoEspecialidad()))
                    .forEach(especialidades::add);
        } else {
            especialidades.addAll(especialidadesAll);
        }
        if (positivo(inputData.getIdTipoDocumento())) {
            filters.put("idTipoDocumento", inputData.getIdTipoDocumento());
            tipoDocumentosAll.stream()
                    .filter(tipoDocumento -> inputData.getIdTipoDocumento().equals(tipoDocumento.getIdTipoDocumento()))
                    .forEach(tipoDocumentos::add);
        } else {
            tipoDocumentos.addAll(tipoDocumentosAll);
        }
        if (positivo(inputData.getIdDocumento())) {
            filters.put("idDocumento", inputData.getIdDocumento());
            documentosAll.stream()
                    .filter(documento -> inputData.getIdDocumento().equals(documento.getIdDocumento()))
                    .forEach(documentos::add);
        } else {
            documentos.addAll(documentosAll);
        }

        List<CabDocumentoPlantillaGenerado> todos = cabDocumentoPlantillaGeneradoDAO.getListDocumentosPlantillaGenerados(
                filters, filtrosFecha(inputData.getFechaInicio(), inputData.getFechaFin()));
        List<CabDocumentoPlantillaGenerado> exitosos = exitosos(todos);

        boolean todasInstancias = response.getCodInstancia().equals("0");

        llenarNodo(response, todos, exitosos, d -> true);

        List<ResponseMainEspecialidad> responseEspecialidades = new ArrayList<>();

        especialidades.forEach(especialidad -> {
            if (!todasInstancias && !response.getCodInstancia().equals(especialidad.getCodigoInstancia())) {
                return;
            }

            ResponseMainEspecialidadPlantilla responseEspecialidad = new ResponseMainEspecialidadPlantilla();
            responseEspecialidad.setCodEspecialidad(especialidad.getCodigoEspecialidad());
            responseEspecialidad.setEspecialidad(especialidad.getEspecialidad());

            Predicate<CabDocumentoPlantillaGenerado> porEspecialidad = doc ->
                    (todasInstancias || Objects.equals(doc.getCodInstancia(), response.getCodInstancia()))
                            && Objects.equals(doc.getCodEspecialidad(), especialidad.getCodigoEspecialidad());
            llenarNodo(responseEspecialidad, todos, exitosos, porEspecialidad);

            List<ResponseMainTipoDoc> tiposIniciales = new ArrayList<>();
            tipoDocumentos.forEach(tipoDocumento -> {
                if (todasInstancias) {
                    ResponseMainTipoDocPlantilla tipo = new ResponseMainTipoDocPlantilla();
                    tipo.setIdTipoDocumento(0L);
                    tipo.setTipoDocumento(tipoDocumento.getDescripcion());
                    tiposIniciales.add(tipo);
                } else if (response.getCodInstancia().equals(tipoDocumento.getIdInstancia())) {
                    ResponseMainTipoDocPlantilla tipo = new ResponseMainTipoDocPlantilla();
                    tipo.setIdTipoDocumento(tipoDocumento.getIdTipoDocumento());
                    tipo.setTipoDocumento(tipoDocumento.getDescripcion());
                    tiposIniciales.add(tipo);
                }
            });

            List<ResponseMainTipoDoc> tipos = tiposIniciales.stream().distinct().collect(Collectors.toList());

            tipos.forEach(tipo -> {
                // Con todas las instancias el tipo se identifica por descripción (idTipoDocumento = 0).
                Predicate<CabDocumentoPlantillaGenerado> porTipo = porEspecialidad.and(doc -> todasInstancias
                        ? Objects.equals(doc.getTipoDocumento(), tipo.getTipoDocumento())
                        : Objects.equals(doc.getIdTipoDocumento(), tipo.getIdTipoDocumento()));
                llenarNodo((ResponseMainTipoDocPlantilla) tipo, todos, exitosos, porTipo);

                List<ResponseMainDoc> documentosIniciales = new ArrayList<>();
                documentos.forEach(documento -> {
                    if (todasInstancias) {
                        tipoDocumentos.forEach(tipoDocumento -> {
                            if (Objects.equals(tipoDocumento.getDescripcion(), tipo.getTipoDocumento())
                                    && Objects.equals(tipoDocumento.getIdTipoDocumento(), documento.getIdTipoDocumento())) {
                                ResponseMainDocPlantilla doc = new ResponseMainDocPlantilla();
                                doc.setIdDocumento(0L);
                                doc.setDocumento(documento.getDescripcion());
                                documentosIniciales.add(doc);
                            }
                        });
                    } else if (Objects.equals(tipo.getIdTipoDocumento(), documento.getIdTipoDocumento())) {
                        ResponseMainDocPlantilla doc = new ResponseMainDocPlantilla();
                        doc.setIdDocumento(documento.getIdDocumento());
                        doc.setDocumento(documento.getDescripcion());
                        documentosIniciales.add(doc);
                    }
                });

                List<ResponseMainDoc> docs = documentosIniciales.stream().distinct().collect(Collectors.toList());

                docs.forEach(doc -> {
                    Predicate<CabDocumentoPlantillaGenerado> porDocumento = porTipo.and(d -> todasInstancias
                            ? Objects.equals(d.getDocumento(), doc.getDocumento())
                            : Objects.equals(d.getIdDocumento(), doc.getIdDocumento()));
                    llenarNodo((ResponseMainDocPlantilla) doc, todos, exitosos, porDocumento);
                });

                tipo.setDocumentos(docs);
            });

            responseEspecialidad.setTipoDocumentos(tipos);
            responseEspecialidades.add(responseEspecialidad);
        });

        response.setEspecialidades(responseEspecialidades);

        return response;
    }

    // ====================================================================
    // Soporte
    // ====================================================================

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

    /**
     * Visibilidad por usuario (misma regla que DocumentoGeneradoServiceImpl): el usuario normal solo
     * ve lo suyo; el resto puede filtrar por un usuario o buscar usuarios por sus datos.
     *
     * @return false si la búsqueda de usuarios no encontró a nadie (el resultado es vacío)
     */
    private boolean filtrosUsuario(ResponseLogin responseLogin, Long idUser, String documento, String nombres,
                                   String apellidos, String cargo, String username, String email,
                                   Map<String, Object> filters) {

        if (Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)) {
            filters.put("userId", responseLogin.getUser().getIdUser());
            return true;
        }
        if (idUser != null && idUser > 0) {
            filters.put("userId", idUser);
            return true;
        }

        InputConsultaIAExternal busqueda = new InputConsultaIAExternal();
        boolean buscarUsuarios = false;

        if (noVacio(documento)) { busqueda.setDocumento(documento); buscarUsuarios = true; }
        if (noVacio(nombres)) { busqueda.setNombres(nombres); buscarUsuarios = true; }
        if (noVacio(apellidos)) { busqueda.setApellidos(apellidos); buscarUsuarios = true; }
        if (noVacio(cargo)) { busqueda.setCargo(cargo); buscarUsuarios = true; }
        if (noVacio(username)) { busqueda.setUsername(username); buscarUsuarios = true; }
        if (noVacio(email)) { busqueda.setEmail(email); buscarUsuarios = true; }

        if (!buscarUsuarios) return true;

        List<OutputConsultaIAExternal> usuarios = securityService.Getusers(busqueda);
        if (usuarios == null || usuarios.isEmpty()) return false;

        filters.put("list_userId", usuarios.stream().map(OutputConsultaIAExternal::getId).toList());
        return true;
    }

    private Map<String, Object> filtrosFecha(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        Map<String, Object> filtersFecha = new HashMap<>();
        if (fechaInicio != null) filtersFecha.put("fechaInicio", fechaInicio);
        if (fechaFin != null) filtersFecha.put("fechaFin", fechaFin);
        return filtersFecha;
    }

    /** codNumero se guarda con 5 dígitos (ceros a la izquierda), igual que en la tabla anterior. */
    private String numeroExpediente(String numero) {
        String valor = numero.trim();
        return "0".repeat(Math.max(0, 5 - valor.length())) + valor;
    }

    private List<CabDocumentoPlantillaGenerado> exitosos(List<CabDocumentoPlantillaGenerado> todos) {
        return todos.stream().filter(d -> Constantes.COMPLETION_EXITOSO.equals(d.getStatus())).toList();
    }

    /** Totales doc/web (solo exitosos) y datos adicionales de un nodo del resumen main. */
    private <T extends ResponseMainNodo> void llenarNodo(T nodo, List<CabDocumentoPlantillaGenerado> todos,
                                                         List<CabDocumentoPlantillaGenerado> exitosos,
                                                         Predicate<CabDocumentoPlantillaGenerado> filtro) {
        List<CabDocumentoPlantillaGenerado> exitososNodo = exitosos.stream().filter(filtro).toList();
        nodo.setTotalDoc(contar(exitososNodo, d -> TYPEDOC_DOC.equals(d.getTypedoc())));
        nodo.setTotalWeb(contar(exitososNodo, d -> TYPEDOC_WEB.equals(d.getTypedoc())));
        llenarExtras(nodo, todos.stream().filter(filtro).toList(), exitososNodo);
    }

    private void llenarExtras(MetricasPlantillaExtra destino, List<CabDocumentoPlantillaGenerado> todos,
                              List<CabDocumentoPlantillaGenerado> exitosos) {
        destino.setTotalErrores(contar(todos, d -> Constantes.COMPLETION_ERROR.equals(d.getStatus())));
        destino.setIaExitoso(contar(exitosos, d -> IA_EXITOSO.equals(d.getEstadoIA())));
        destino.setIaError(contar(exitosos, d -> IA_ERROR.equals(d.getEstadoIA())));
        destino.setTotalTokens(exitosos.stream().mapToLong(d -> n(d.getTotalTokens())).sum());

        OptionalDouble promedio = exitosos.stream().filter(d -> d.getTiempoTotalMs() != null)
                .mapToLong(CabDocumentoPlantillaGenerado::getTiempoTotalMs).average();
        destino.setTiempoPromedioTotalMs(promedio.isPresent()
                ? BigDecimal.valueOf(promedio.getAsDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue()
                : null);
    }

    private static long contar(List<CabDocumentoPlantillaGenerado> datos, Predicate<CabDocumentoPlantillaGenerado> filtro) {
        return datos.stream().filter(filtro).count();
    }

    private static long n(Number valor) {
        return valor == null ? 0L : valor.longValue();
    }

    private static boolean noVacio(String valor) {
        return valor != null && !valor.isEmpty();
    }

    private static boolean positivo(Long valor) {
        return valor != null && valor > 0;
    }
}
