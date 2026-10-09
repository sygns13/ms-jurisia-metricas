package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaDetalle;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaFiltros;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaMetricas;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorDocumento;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorExpediente;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorInstancia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consultas de los reportes admin de documentos generados por plantilla (CabDocumentoPlantillaGenerado).
 * Los nombres de columna coinciden con los atributos de la entidad, por eso el mismo WHERE sirve
 * para las consultas nativas agrupadas y para la consulta JPQL del detalle.
 */
@Component
public class DocPlantillaAdminReporteQuery {

    // Métricas comunes (status: 0 iniciado, 1 exitoso, 2 error). Mismo orden que ResponseAdminDocPlantillaMetricas.
    private static final String METRICAS =
            " SUM(CASE WHEN d.status = 1 THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.status = 2 THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.status = 0 OR d.status IS NULL THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.typedoc = 'doc' THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.typedoc = 'web' THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.corregirIA = 1 THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.estadoIA = 'EXITOSO' THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.estadoIA = 'ERROR' THEN 1 ELSE 0 END), " +
            " SUM(CASE WHEN d.estadoIA = 'NO_APLICA' THEN 1 ELSE 0 END), " +
            " SUM(d.parrafosEnviadosIA), SUM(d.parrafosCorregidosIA), SUM(d.bloquesSolicitadosIA), SUM(d.bloquesGeneradosIA), " +
            " SUM(d.promptTokens), SUM(d.candidatesTokens), SUM(d.thoughtsTokens), SUM(d.cachedTokens), SUM(d.totalTokens), " +
            " SUM(d.totalVariables), SUM(d.variablesSij), SUM(d.variablesCalculadas), SUM(d.variablesManuales), " +
            " SUM(d.variablesIA), SUM(d.variablesNoDefinidas), SUM(d.variablesSinValor), SUM(d.reemplazosRealizados), " +
            " AVG(CASE WHEN d.status = 1 THEN d.tiempoTotalMs END), " +
            " MIN(CASE WHEN d.status = 1 THEN d.tiempoTotalMs END), " +
            " MAX(CASE WHEN d.status = 1 THEN d.tiempoTotalMs END), " +
            " AVG(CASE WHEN d.status = 1 THEN d.tiempoSijMs END), " +
            " AVG(CASE WHEN d.status = 1 THEN d.tiempoIAMs END), " +
            " AVG(d.tamanioSalidaBytes), " +
            " MIN(d.regDatetime), MAX(d.regDatetime), " +
            " GROUP_CONCAT(DISTINCT CONCAT(TRIM(d.codigoPlantilla), ' v', d.versionPlantilla) ORDER BY d.codigoPlantilla SEPARATOR ', '), " +
            " GROUP_CONCAT(DISTINCT TRIM(d.model) ORDER BY d.model SEPARATOR ', ') ";

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ResponseAdminDocPlantillaPorInstancia> agrupadoPorInstancia(InputAdminDocPlantillaPorInstancia input,
                                                                            Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        String where = construirWhere(input.getFechaInicial(), input.getFechaFinal(), input.getCodSede(),
                input.getCodInstancia(), null, null, null, null, null, params);

        String from = " FROM CabDocumentoPlantillaGenerado d " + where + " GROUP BY d.codSede, d.codInstancia ";

        String sql = "SELECT d.codSede, MAX(d.sede) AS sedeDesc, d.codInstancia, MAX(d.instancia) AS instanciaDesc, " +
                " COUNT(DISTINCT d.userId), COUNT(DISTINCT d.nUnico), COUNT(*), " +
                " COUNT(DISTINCT d.idTipoDocumento), COUNT(DISTINCT d.idDocumento), COUNT(DISTINCT d.codMateria), " +
                METRICAS + from +
                " ORDER BY sedeDesc, instanciaDesc, d.codInstancia ";

        List<Object[]> rows = paginar(crearQuery(sql, params), pageable).getResultList();

        List<ResponseAdminDocPlantillaPorInstancia> content = rows.stream().map(r -> {
            ResponseAdminDocPlantillaPorInstancia item = new ResponseAdminDocPlantillaPorInstancia();
            item.setCodSede(texto(r[0]));
            item.setSede(texto(r[1]));
            item.setCodInstancia(texto(r[2]));
            item.setInstancia(texto(r[3]));
            item.setTotalUsuarios(entero(r[4]));
            item.setTotalExpedientes(entero(r[5]));
            item.setTotalDocumentos(entero(r[6]));
            item.setTotalTiposDocumento(entero(r[7]));
            item.setTotalDocumentosDistintos(entero(r[8]));
            item.setTotalMaterias(entero(r[9]));
            llenarMetricas(item, r, 10, item.getTotalDocumentos());
            return item;
        }).toList();

        return new PageImpl<>(content, pageable, contarGrupos(from, params));
    }

    public Page<ResponseAdminDocPlantillaPorExpediente> agrupadoPorExpediente(InputAdminDocPlantillaFiltros input,
                                                                              Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        String where = construirWhere(input.getFechaInicial(), input.getFechaFinal(), input.getCodSede(),
                input.getCodInstancia(), input.getIdUser(), input.getExpNro(), input.getAnio(),
                input.getIdTipoDocumento(), input.getIdDocumento(), params);

        // Una fila por expediente y usuario; los datos descriptivos se toman con MAX porque son
        // constantes dentro del grupo (ONLY_FULL_GROUP_BY).
        String from = " FROM CabDocumentoPlantillaGenerado d " + where +
                " GROUP BY d.codSede, d.codInstancia, d.nUnico, d.userId ";

        String sql = "SELECT d.codSede, MAX(d.sede) AS sedeDesc, d.codInstancia, MAX(d.instancia) AS instanciaDesc, " +
                " d.nUnico, MAX(d.xFormato) AS xformatoDesc, MAX(d.codMateria), MAX(d.materia), " +
                " d.userId, MAX(d.username), MAX(d.nombreUsuario) AS usuarioDesc, MAX(d.cargo), MAX(d.dependencia), " +
                " COUNT(*), MAX(d.codYear), MAX(d.codNumero), MAX(d.numIncidente), MAX(d.codEspecialidad), " +
                " MAX(d.especialidad), MAX(d.ubicacion), MAX(d.estado), MAX(d.juez), MAX(d.especialista), " +
                " MAX(d.demandante), MAX(d.demandado), " +
                " COUNT(DISTINCT d.idTipoDocumento), COUNT(DISTINCT d.idDocumento), " +
                " GROUP_CONCAT(DISTINCT CONCAT(TRIM(d.tipoDocumento), ' - ', TRIM(d.documento)) ORDER BY d.tipoDocumento, d.documento SEPARATOR ', '), " +
                " MAX(d.id), " +
                METRICAS + from +
                " ORDER BY sedeDesc, instanciaDesc, xformatoDesc, usuarioDesc, d.userId ";

        List<Object[]> rows = paginar(crearQuery(sql, params), pageable).getResultList();

        List<ResponseAdminDocPlantillaPorExpediente> content = rows.stream().map(r -> {
            ResponseAdminDocPlantillaPorExpediente item = new ResponseAdminDocPlantillaPorExpediente();
            item.setCodSede(texto(r[0]));
            item.setSede(texto(r[1]));
            item.setCodInstancia(texto(r[2]));
            item.setInstancia(texto(r[3]));
            item.setNUnico(enteroNulo(r[4]));
            item.setXFormato(texto(r[5]));
            item.setCodMateria(texto(r[6]));
            item.setMateria(texto(r[7]));
            item.setIdUser(enteroNulo(r[8]));
            item.setUsername(texto(r[9]));
            item.setNombreUsuario(texto(r[10]));
            item.setCargo(texto(r[11]));
            item.setDependencia(texto(r[12]));
            item.setTotalDocumentos(entero(r[13]));
            item.setAnio(texto(r[14]));
            item.setExpNro(texto(r[15]));
            item.setNumIncidente(texto(r[16]));
            item.setCodEspecialidad(texto(r[17]));
            item.setEspecialidad(texto(r[18]));
            item.setUbicacion(texto(r[19]));
            item.setEstado(texto(r[20]));
            item.setJuez(texto(r[21]));
            item.setEspecialista(texto(r[22]));
            item.setDemandante(texto(r[23]));
            item.setDemandado(texto(r[24]));
            item.setTotalTiposDocumento(entero(r[25]));
            item.setTotalDocumentosDistintos(entero(r[26]));
            item.setDocumentosGenerados(texto(r[27]));
            item.setIdUltimoRegistro(enteroNulo(r[28]));
            llenarMetricas(item, r, 29, item.getTotalDocumentos());
            return item;
        }).toList();

        return new PageImpl<>(content, pageable, contarGrupos(from, params));
    }

    public Page<ResponseAdminDocPlantillaPorDocumento> agrupadoPorDocumento(InputAdminDocPlantillaFiltros input,
                                                                            Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        String where = construirWhere(input.getFechaInicial(), input.getFechaFinal(), input.getCodSede(),
                input.getCodInstancia(), input.getIdUser(), input.getExpNro(), input.getAnio(),
                input.getIdTipoDocumento(), input.getIdDocumento(), params);

        // Sin documento elegido se agrupa solo por tipo de documento y el documento se informa como TODOS.
        boolean porDocumento = input.getIdDocumento() != null;

        String from = " FROM CabDocumentoPlantillaGenerado d " + where +
                " GROUP BY d.codSede, d.codInstancia, d.idTipoDocumento" + (porDocumento ? ", d.idDocumento " : " ");

        String sql = "SELECT d.codSede, MAX(d.sede) AS sedeDesc, d.codInstancia, MAX(d.instancia) AS instanciaDesc, " +
                " d.idTipoDocumento, MAX(d.tipoDocumento) AS tipoDesc, " +
                (porDocumento ? " d.idDocumento, MAX(d.documento) AS documentoDesc, " : " NULL, NULL AS documentoDesc, ") +
                " COUNT(DISTINCT d.codMateria), " +
                " GROUP_CONCAT(DISTINCT TRIM(d.materia) ORDER BY d.materia SEPARATOR ', '), " +
                " COUNT(DISTINCT d.userId), " +
                " GROUP_CONCAT(DISTINCT TRIM(d.username) ORDER BY d.username SEPARATOR ', '), " +
                " COUNT(*), " +
                " GROUP_CONCAT(DISTINCT TRIM(d.codYear) ORDER BY d.codYear SEPARATOR ', '), " +
                " COUNT(DISTINCT d.nUnico), " +
                " GROUP_CONCAT(DISTINCT TRIM(d.xFormato) ORDER BY d.xFormato SEPARATOR ', '), " +
                METRICAS + from +
                " ORDER BY sedeDesc, instanciaDesc, tipoDesc, documentoDesc ";

        List<Object[]> rows = paginar(crearQuery(sql, params), pageable).getResultList();

        List<ResponseAdminDocPlantillaPorDocumento> content = rows.stream().map(r -> {
            ResponseAdminDocPlantillaPorDocumento item = new ResponseAdminDocPlantillaPorDocumento();
            item.setCodSede(texto(r[0]));
            item.setSede(texto(r[1]));
            item.setCodInstancia(texto(r[2]));
            item.setInstancia(texto(r[3]));
            item.setIdTipoDocumento(enteroNulo(r[4]));
            item.setTipoDocumento(texto(r[5]));
            item.setIdDocumento(porDocumento ? enteroNulo(r[6]) : null);
            item.setDocumento(porDocumento ? texto(r[7]) : ResponseAdminDocPlantillaPorDocumento.DOCUMENTO_TODOS);
            item.setTotalMaterias(entero(r[8]));
            item.setMaterias(texto(r[9]));
            item.setTotalUsuarios(entero(r[10]));
            item.setUsuarios(texto(r[11]));
            item.setTotalDocumentos(entero(r[12]));
            item.setAnios(texto(r[13]));
            item.setTotalExpedientes(entero(r[14]));
            item.setExpedientes(texto(r[15]));
            llenarMetricas(item, r, 16, item.getTotalDocumentos());
            return item;
        }).toList();

        return new PageImpl<>(content, pageable, contarGrupos(from, params));
    }

    public Page<CabDocumentoPlantillaGenerado> detalle(InputAdminDocPlantillaDetalle input, Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        String where = construirWhere(input.getFechaInicial(), input.getFechaFinal(), input.getCodSede(),
                input.getCodInstancia(), input.getIdUser(), input.getExpNro(), input.getAnio(),
                input.getIdTipoDocumento(), input.getIdDocumento(), params);

        Query query = entityManager.createQuery(
                "SELECT d FROM CabDocumentoPlantillaGenerado d " + where + " ORDER BY d.regDatetime DESC, d.id DESC",
                CabDocumentoPlantillaGenerado.class);
        params.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<CabDocumentoPlantillaGenerado> content = paginar(query, pageable).getResultList();

        Query count = entityManager.createQuery("SELECT COUNT(d) FROM CabDocumentoPlantillaGenerado d " + where);
        params.forEach(count::setParameter);

        return new PageImpl<>(content, pageable, entero(count.getSingleResult()));
    }

    private String construirWhere(LocalDate fechaInicial, LocalDate fechaFinal, String codSede, String codInstancia,
                                  Long idUser, String expNro, String anio, Long idTipoDocumento, Long idDocumento,
                                  Map<String, Object> params) {

        StringBuilder where = new StringBuilder(" WHERE d.regDate >= :fechaInicial AND d.regDate <= :fechaFinal ");
        params.put("fechaInicial", fechaInicial);
        params.put("fechaFinal", fechaFinal);

        if (noVacio(codSede)) {
            where.append(" AND d.codSede = :codSede ");
            params.put("codSede", codSede.trim());
        }
        if (noVacio(codInstancia)) {
            where.append(" AND d.codInstancia = :codInstancia ");
            params.put("codInstancia", codInstancia.trim());
        }
        if (idUser != null && idUser > 0) {
            where.append(" AND d.userId = :userId ");
            params.put("userId", idUser);
        }
        if (noVacio(expNro)) {
            where.append(" AND d.codNumero LIKE :expNro ");
            params.put("expNro", "%" + expNro.trim() + "%");
        }
        if (noVacio(anio)) {
            where.append(" AND d.codYear = :anio ");
            params.put("anio", anio.trim());
        }
        if (idTipoDocumento != null) {
            where.append(" AND d.idTipoDocumento = :idTipoDocumento ");
            params.put("idTipoDocumento", idTipoDocumento);
        }
        if (idDocumento != null) {
            where.append(" AND d.idDocumento = :idDocumento ");
            params.put("idDocumento", idDocumento);
        }
        return where.toString();
    }

    private void llenarMetricas(ResponseAdminDocPlantillaMetricas m, Object[] r, int i, Long total) {
        m.setTotalExitosos(entero(r[i]));
        m.setTotalErrores(entero(r[i + 1]));
        m.setTotalEnProceso(entero(r[i + 2]));
        m.setTotalDoc(entero(r[i + 3]));
        m.setTotalWeb(entero(r[i + 4]));
        m.setTotalConCorreccionIA(entero(r[i + 5]));
        m.setIaExitoso(entero(r[i + 6]));
        m.setIaError(entero(r[i + 7]));
        m.setIaNoAplica(entero(r[i + 8]));
        m.setParrafosEnviadosIA(entero(r[i + 9]));
        m.setParrafosCorregidosIA(entero(r[i + 10]));
        m.setBloquesSolicitadosIA(entero(r[i + 11]));
        m.setBloquesGeneradosIA(entero(r[i + 12]));
        m.setPromptTokens(entero(r[i + 13]));
        m.setCandidatesTokens(entero(r[i + 14]));
        m.setThoughtsTokens(entero(r[i + 15]));
        m.setCachedTokens(entero(r[i + 16]));
        m.setTotalTokens(entero(r[i + 17]));
        m.setTotalVariables(entero(r[i + 18]));
        m.setVariablesSij(entero(r[i + 19]));
        m.setVariablesCalculadas(entero(r[i + 20]));
        m.setVariablesManuales(entero(r[i + 21]));
        m.setVariablesIA(entero(r[i + 22]));
        m.setVariablesNoDefinidas(entero(r[i + 23]));
        m.setVariablesSinValor(entero(r[i + 24]));
        m.setReemplazosRealizados(entero(r[i + 25]));
        m.setTiempoPromedioTotalMs(decimal(r[i + 26]));
        m.setTiempoMinimoTotalMs(enteroNulo(r[i + 27]));
        m.setTiempoMaximoTotalMs(enteroNulo(r[i + 28]));
        m.setTiempoPromedioSijMs(decimal(r[i + 29]));
        m.setTiempoPromedioIAMs(decimal(r[i + 30]));
        m.setTamanioPromedioSalidaBytes(decimal(r[i + 31]));
        m.setFechaPrimeraGeneracion(fecha(r[i + 32]));
        m.setFechaUltimaGeneracion(fecha(r[i + 33]));
        m.setPlantillasUtilizadas(texto(r[i + 34]));
        m.setModelosUtilizados(texto(r[i + 35]));
        m.setPorcentajeExito(total == null || total == 0 ? 0.0
                : BigDecimal.valueOf(m.getTotalExitosos() * 100.0 / total).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    private Long contarGrupos(String from, Map<String, Object> params) {
        return entero(crearQuery("SELECT COUNT(*) FROM (SELECT 1 " + from + ") grupos", params).getSingleResult());
    }

    private Query crearQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        return query;
    }

    private Query paginar(Query query, Pageable pageable) {
        return query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
    }

    private static boolean noVacio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private static String texto(Object valor) {
        return valor != null ? valor.toString().trim() : null;
    }

    private static Long entero(Object valor) {
        return valor != null ? ((Number) valor).longValue() : 0L;
    }

    private static Long enteroNulo(Object valor) {
        return valor != null ? ((Number) valor).longValue() : null;
    }

    private static Double decimal(Object valor) {
        if (valor == null) {
            return null;
        }
        return BigDecimal.valueOf(((Number) valor).doubleValue()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static LocalDateTime fecha(Object valor) {
        if (valor instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        return (LocalDateTime) valor;
    }
}
