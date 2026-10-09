package pj.gob.pe.metricas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.service.business.DemandasSentenciasService;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.inputs.demandasadmin.InputAdminDemandasPorInstancia;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorExpediente;
import pj.gob.pe.metricas.utils.responses.demandasadmin.ResponseAdminDemandasPorInstancia;

import java.util.List;

@Tag(name = "Admin Demandas Sentencias Controller", description = "Reportes de administrador para Sentencias de Demandas")
@RestController
@RequestMapping("/v1/admin/demandas-sentencias")
@RequiredArgsConstructor
public class DemandasSentenciasAdminController {

    private final DemandasSentenciasService demandasSentenciasService;

    @Operation(summary = "Sentencias agrupadas por sede e instancia",
            description = "Reporte paginado de administrador. Filtros: fechaInicial y fechaFinal (yyyy-MM-dd, obligatorias, " +
                    "contra fechaSend), codSede y cinstancia (opcionales, exactos). Por instancia devuelve usuarios, " +
                    "expedientes y sentencias totales, desglose por status, tiempos y fechas de la primera/última ejecución. " +
                    "Ordenado por sede e instancia. Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/agrupado-por-instancia")
    public ResponseEntity<Page<ResponseAdminDemandasPorInstancia>> reportePorInstancia(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDemandasPorInstancia inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ResponseAdminDemandasPorInstancia> dataResponse =
                demandasSentenciasService.reporteAdminPorInstancia(SessionId, inputData, pageable);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Sentencias agrupadas por expediente y usuario",
            description = "Reporte paginado de administrador. Filtros: fechaInicial, fechaFinal (yyyy-MM-dd, contra fechaSend), " +
                    "codSede y cinstancia obligatorios; idUser, expNro (LIKE) y anio opcionales. Una fila por expediente " +
                    "(nUnico) y usuario con el total de sentencias, desglose por status, tiempos, datos del expediente " +
                    "y de la última versión. Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/agrupado-por-expediente")
    public ResponseEntity<Page<ResponseAdminDemandasPorExpediente>> reportePorExpediente(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDemandasPorExpediente inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ResponseAdminDemandasPorExpediente> dataResponse =
                demandasSentenciasService.reporteAdminPorExpediente(SessionId, inputData, pageable);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Detalle de todas las sentencias de un expediente",
            description = "Todas las versiones de sentencia del nUnico (de todos los usuarios), de la más nueva a la más " +
                    "antigua, con todos los datos registrados: sentencia (response), prompt de sistema, modelo, tiempos, " +
                    "datos del expediente, sede/instancia y del usuario. Solo para Super Administrador, Administrador y Reportes.")
    @GetMapping("/detalle-por-nunico")
    public ResponseEntity<List<DemandasSentenciasToKafka>> detallePorNunico(
            @RequestHeader("SessionId") String SessionId,
            @RequestParam(name = "nunico") Long nunico) {

        List<DemandasSentenciasToKafka> dataResponse = demandasSentenciasService.detalleAdminPorNunico(SessionId, nunico);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
