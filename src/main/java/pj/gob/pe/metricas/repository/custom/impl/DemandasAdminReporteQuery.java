package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorInstancia;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorInstancia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consultas nativas de los reportes admin de calificaciones y sentencias. DemandasCalificadas y
 * DemandasSentencias tienen la misma estructura, por eso la misma SQL se reutiliza para ambas
 * cambiando solo la tabla (que viene de {@link Tabla}, nunca del request).
 */
@Component
public class DemandasAdminReporteQuery {

    public enum Tabla {
        CALIFICACIONES("DemandasCalificadas"),
        SENTENCIAS("DemandasSentencias");

        private final String nombre;

        Tabla(String nombre) {
            this.nombre = nombre;
        }
    }

    // Métricas comunes a ambas agrupaciones (status: 0 iniciada, 1 exitosa, 2 error archivo, 3 error Gemini).
    private static final String METRICAS =
            " SUM(CASE WHEN d.status = 1 THEN 1 ELSE 0 END) AS totalExitosas, " +
            " SUM(CASE WHEN d.status = 2 THEN 1 ELSE 0 END) AS totalErrorArchivo, " +
            " SUM(CASE WHEN d.status = 3 THEN 1 ELSE 0 END) AS totalErrorIA, " +
            " SUM(CASE WHEN d.status = 0 OR d.status IS NULL THEN 1 ELSE 0 END) AS totalEnProceso, " +
            " AVG(d.timeSeconds) AS tiempoPromedio, " +
            " MIN(d.timeSeconds) AS tiempoMinimo, " +
            " MAX(d.timeSeconds) AS tiempoMaximo, " +
            " SUM(d.timeSeconds) AS tiempoTotal, " +
            " MIN(d.fechaSend) AS fechaPrimera, " +
            " MAX(d.fechaSend) AS fechaUltima, " +
            " GROUP_CONCAT(DISTINCT TRIM(d.model) ORDER BY d.model SEPARATOR ', ') AS modelos ";

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ResponseAdminDemandasPorInstancia> agrupadoPorInstancia(Tabla tabla,
                                                                         InputAdminDemandasPorInstancia input,
                                                                         Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        StringBuilder where = filtroFechas(input.getFechaInicial().atStartOfDay(),
                input.getFechaFinal().atTime(LocalTime.MAX), params);

        if (input.getCodSede() != null && !input.getCodSede().trim().isEmpty()) {
            where.append(" AND d.codSede = :codSede ");
            params.put("codSede", input.getCodSede().trim());
        }
        if (input.getCinstancia() != null && !input.getCinstancia().trim().isEmpty()) {
            where.append(" AND d.cinstancia = :cinstancia ");
            params.put("cinstancia", input.getCinstancia().trim());
        }

        String from = " FROM " + tabla.nombre + " d " + where + " GROUP BY d.codSede, d.cinstancia ";

        String sql = "SELECT d.codSede, MAX(d.sede) AS sedeDesc, d.cinstancia, MAX(d.xnomInstancia) AS instanciaDesc, " +
                " COUNT(DISTINCT d.userId) AS totalUsuarios, " +
                " COUNT(DISTINCT d.nUnico) AS totalExpedientes, " +
                " COUNT(*) AS totalRegistros, " +
                " COUNT(DISTINCT d.cmateria) AS totalMaterias, " +
                METRICAS + from +
                " ORDER BY sedeDesc, instanciaDesc, d.cinstancia ";

        List<Object[]> rows = paginar(crearQuery(sql, params), pageable).getResultList();

        List<ResponseAdminDemandasPorInstancia> content = rows.stream().map(r -> {
            ResponseAdminDemandasPorInstancia item = new ResponseAdminDemandasPorInstancia();
            item.setCodSede(texto(r[0]));
            item.setSede(texto(r[1]));
            item.setCinstancia(texto(r[2]));
            item.setXnomInstancia(texto(r[3]));
            item.setTotalUsuarios(entero(r[4]));
            item.setTotalExpedientes(entero(r[5]));
            item.setTotalRegistros(entero(r[6]));
            item.setTotalMaterias(entero(r[7]));
            item.setTotalExitosas(entero(r[8]));
            item.setTotalErrorArchivo(entero(r[9]));
            item.setTotalErrorIA(entero(r[10]));
            item.setTotalEnProceso(entero(r[11]));
            item.setTiempoPromedioSegundos(decimal(r[12]));
            item.setTiempoMinimoSegundos(decimal(r[13]));
            item.setTiempoMaximoSegundos(decimal(r[14]));
            item.setTiempoTotalSegundos(decimal(r[15]));
            item.setFechaPrimeraOperacion(fecha(r[16]));
            item.setFechaUltimaOperacion(fecha(r[17]));
            item.setModelosUtilizados(texto(r[18]));
            item.setPorcentajeExito(dividir(item.getTotalExitosas() * 100.0, item.getTotalRegistros()));
            item.setPromedioPorExpediente(dividir(item.getTotalRegistros(), item.getTotalExpedientes()));
            return item;
        }).toList();

        return new PageImpl<>(content, pageable, contarGrupos(from, params));
    }

    public Page<ResponseAdminDemandasPorExpediente> agrupadoPorExpediente(Tabla tabla,
                                                                           InputAdminDemandasPorExpediente input,
                                                                           Pageable pageable) {
        Map<String, Object> params = new LinkedHashMap<>();
        StringBuilder where = filtroFechas(input.getFechaInicial().atStartOfDay(),
                input.getFechaFinal().atTime(LocalTime.MAX), params);

        where.append(" AND d.codSede = :codSede AND d.cinstancia = :cinstancia ");
        params.put("codSede", input.getCodSede().trim());
        params.put("cinstancia", input.getCinstancia().trim());

        if (input.getIdUser() != null && input.getIdUser() > 0) {
            where.append(" AND d.userId = :userId ");
            params.put("userId", input.getIdUser());
        }
        if (input.getExpNro() != null && !input.getExpNro().trim().isEmpty()) {
            where.append(" AND d.expNro LIKE :expNro ");
            params.put("expNro", "%" + input.getExpNro().trim() + "%");
        }
        if (input.getAnio() != null && !input.getAnio().trim().isEmpty()) {
            where.append(" AND d.anio = :anio ");
            params.put("anio", input.getAnio().trim());
        }

        // Una fila por expediente y usuario; los datos descriptivos se toman con MAX porque son
        // constantes dentro del grupo (ONLY_FULL_GROUP_BY).
        String from = " FROM " + tabla.nombre + " d " + where +
                " GROUP BY d.codSede, d.cinstancia, d.nUnico, d.userId ";

        String sql = "SELECT d.codSede, MAX(d.sede) AS sedeDesc, d.cinstancia, MAX(d.xnomInstancia) AS instanciaDesc, " +
                " d.nUnico, MAX(d.xformato) AS xformatoDesc, MAX(d.cmateria), MAX(d.xdescMateria), MAX(d.tipoExpediente), " +
                " d.userId, MAX(d.username), MAX(d.nombres), MAX(d.apellidos) AS apellidosDesc, MAX(d.documento), " +
                " MAX(d.cargo), MAX(d.nombreDependencia), " +
                " COUNT(*) AS totalRegistros, MAX(d.anio), MAX(d.expNro), MAX(d.nincidente), MAX(d.cespecialidad), " +
                " MAX(d.xdescUbicacion), MAX(d.xdescEstado), MAX(d.xdescJuez), MAX(d.xdescEspecialista), " +
                " MAX(d.xdescDemandante), MAX(d.xdescDemandado), " +
                METRICAS + ", " +
                " MAX(d.id) AS idUltimo, " +
                " SUBSTRING_INDEX(GROUP_CONCAT(d.status ORDER BY d.id DESC), ',', 1) AS statusUltimo " +
                from +
                " ORDER BY sedeDesc, instanciaDesc, xformatoDesc, apellidosDesc, d.userId ";

        List<Object[]> rows = paginar(crearQuery(sql, params), pageable).getResultList();

        List<ResponseAdminDemandasPorExpediente> content = rows.stream().map(r -> {
            ResponseAdminDemandasPorExpediente item = new ResponseAdminDemandasPorExpediente();
            item.setCodSede(texto(r[0]));
            item.setSede(texto(r[1]));
            item.setCinstancia(texto(r[2]));
            item.setXnomInstancia(texto(r[3]));
            item.setNUnico(enteroNulo(r[4]));
            item.setXformato(texto(r[5]));
            item.setCmateria(texto(r[6]));
            item.setXdescMateria(texto(r[7]));
            item.setTipoExpediente(texto(r[8]));
            item.setIdUser(enteroNulo(r[9]));
            item.setUsername(texto(r[10]));
            item.setNombres(texto(r[11]));
            item.setApellidos(texto(r[12]));
            item.setDocumento(texto(r[13]));
            item.setCargo(texto(r[14]));
            item.setNombreDependencia(texto(r[15]));
            item.setTotalRegistros(entero(r[16]));
            item.setAnio(texto(r[17]));
            item.setExpNro(texto(r[18]));
            item.setNincidente(texto(r[19]));
            item.setCespecialidad(texto(r[20]));
            item.setXdescUbicacion(texto(r[21]));
            item.setXdescEstado(texto(r[22]));
            item.setXdescJuez(texto(r[23]));
            item.setXdescEspecialista(texto(r[24]));
            item.setXdescDemandante(texto(r[25]));
            item.setXdescDemandado(texto(r[26]));
            item.setTotalExitosas(entero(r[27]));
            item.setTotalErrorArchivo(entero(r[28]));
            item.setTotalErrorIA(entero(r[29]));
            item.setTotalEnProceso(entero(r[30]));
            item.setTiempoPromedioSegundos(decimal(r[31]));
            item.setTiempoMinimoSegundos(decimal(r[32]));
            item.setTiempoMaximoSegundos(decimal(r[33]));
            item.setTiempoTotalSegundos(decimal(r[34]));
            item.setFechaPrimeraOperacion(fecha(r[35]));
            item.setFechaUltimaOperacion(fecha(r[36]));
            item.setModelosUtilizados(texto(r[37]));
            item.setIdUltimoRegistro(enteroNulo(r[38]));
            Long statusUltimo = r[39] != null && !r[39].toString().isEmpty() ? Long.valueOf(r[39].toString()) : null;
            item.setStatusUltimoRegistro(statusUltimo != null ? statusUltimo.intValue() : null);
            return item;
        }).toList();

        return new PageImpl<>(content, pageable, contarGrupos(from, params));
    }

    private StringBuilder filtroFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta, Map<String, Object> params) {
        params.put("fechaDesde", fechaDesde);
        params.put("fechaHasta", fechaHasta);
        return new StringBuilder(" WHERE d.fechaSend >= :fechaDesde AND d.fechaSend <= :fechaHasta ");
    }

    private Long contarGrupos(String from, Map<String, Object> params) {
        Object total = crearQuery("SELECT COUNT(*) FROM (SELECT 1 " + from + ") grupos", params).getSingleResult();
        return entero(total);
    }

    private Query crearQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        return query;
    }

    private Query paginar(Query query, Pageable pageable) {
        return query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
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

    private static Double dividir(double numerador, Long denominador) {
        if (denominador == null || denominador == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf(numerador / denominador).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static LocalDateTime fecha(Object valor) {
        if (valor instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        return (LocalDateTime) valor;
    }
}
