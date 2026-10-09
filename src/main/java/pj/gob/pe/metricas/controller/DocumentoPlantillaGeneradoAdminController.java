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
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.service.business.DocumentoPlantillaGeneradoService;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaDetalle;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaFiltros;
import pj.gob.pe.metricas.utils.inputs.docplantillaadmin.InputAdminDocPlantillaPorInstancia;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorDocumento;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorExpediente;
import pj.gob.pe.metricas.utils.responses.docplantillaadmin.ResponseAdminDocPlantillaPorInstancia;

@Tag(name = "Admin Documento Plantilla Generado Controller", description = "Reportes de administrador para documentos generados por plantilla Word completa")
@RestController
@RequestMapping("/v1/admin/documento-plantilla-generado")
@RequiredArgsConstructor
public class DocumentoPlantillaGeneradoAdminController {

    private final DocumentoPlantillaGeneradoService documentoPlantillaGeneradoService;

    @Operation(summary = "Documentos generados agrupados por sede e instancia",
            description = "Reporte paginado de administrador. Filtros: fechaInicial y fechaFinal (yyyy-MM-dd, obligatorias, " +
                    "contra regDate), codSede y codInstancia (opcionales, exactos). Por instancia devuelve usuarios, " +
                    "expedientes y documentos generados, desglose por status/typedoc/IA, tokens, variables, tiempos y " +
                    "plantillas. Ordenado por sede e instancia. Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/agrupado-por-instancia")
    public ResponseEntity<Page<ResponseAdminDocPlantillaPorInstancia>> reportePorInstancia(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDocPlantillaPorInstancia inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(documentoPlantillaGeneradoService.reporteAdminPorInstancia(SessionId, inputData, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Documentos generados agrupados por expediente y usuario",
            description = "Reporte paginado de administrador. Filtros: fechaInicial, fechaFinal, codSede y codInstancia " +
                    "obligatorios; idUser, expNro (LIKE), anio, idTipoDocumento e idDocumento opcionales. Una fila por " +
                    "expediente (nUnico) y usuario con el total de documentos generados, datos del expediente y métricas. " +
                    "Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/agrupado-por-expediente")
    public ResponseEntity<Page<ResponseAdminDocPlantillaPorExpediente>> reportePorExpediente(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDocPlantillaFiltros inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(documentoPlantillaGeneradoService.reporteAdminPorExpediente(SessionId, inputData, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Documentos generados agrupados por tipo de documento y documento",
            description = "Reporte paginado de administrador. Mismos filtros que el reporte por expediente. Si no se envía " +
                    "idDocumento se agrupa por tipo de documento y el documento se informa como TODOS; si se envía, por " +
                    "tipo de documento y documento. Incluye materias, usuarios, años y expedientes involucrados y métricas. " +
                    "Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/agrupado-por-documento")
    public ResponseEntity<Page<ResponseAdminDocPlantillaPorDocumento>> reportePorDocumento(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDocPlantillaFiltros inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(documentoPlantillaGeneradoService.reporteAdminPorDocumento(SessionId, inputData, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Detalle de documentos generados (filas de CabDocumentoPlantillaGenerado)",
            description = "Listado paginado de administrador, más recientes primero, con todas las columnas registradas. " +
                    "Filtros: fechaInicial y fechaFinal obligatorias; codSede, codInstancia, idUser, expNro (LIKE), anio, " +
                    "idTipoDocumento e idDocumento opcionales. Las variables de cada fila se consultan con " +
                    "/v1/documento-plantilla-generado/{id}/variables. Solo para Super Administrador, Administrador y Reportes.")
    @PostMapping("/detalle")
    public ResponseEntity<Page<CabDocumentoPlantillaGenerado>> detalle(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputAdminDocPlantillaDetalle inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(documentoPlantillaGeneradoService.detalleAdmin(SessionId, inputData, pageable), HttpStatus.OK);
    }
}
