package pj.gob.pe.metricas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.model.entities.DetDocumentoPlantillaVariable;
import pj.gob.pe.metricas.service.business.DocumentoPlantillaGeneradoService;
import pj.gob.pe.metricas.utils.inputs.docplantilla.InputDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.utils.responses.docplantilla.ResponseResumenDocPlantilla;

import java.util.List;

@Tag(name = "Documento Plantilla Generado Controller", description = "Métricas de documentos generados por plantilla Word completa")
@RestController
@RequestMapping("/v1/documento-plantilla-generado")
@RequiredArgsConstructor
public class DocumentoPlantillaGeneradoController {

    private final DocumentoPlantillaGeneradoService documentoPlantillaGeneradoService;

    @Operation(summary = "Listado de documentos generados por plantilla", description = "Listado paginado (más recientes primero) con filtros")
    @PostMapping("/get-data")
    public ResponseEntity<Page<CabDocumentoPlantillaGenerado>> getData(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDocumentoPlantillaGenerado inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");

        return new ResponseEntity<>(documentoPlantillaGeneradoService.getDocumentosPlantillaGenerados(SessionId, inputData, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Variables de un documento generado", description = "Cómo se resolvió cada variable (SIJ, CALCULADA, MANUAL, IA, NO_DEFINIDA) y si quedó con valor")
    @GetMapping("/{id}/variables")
    public ResponseEntity<List<DetDocumentoPlantillaVariable>> getVariables(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception {

        return new ResponseEntity<>(documentoPlantillaGeneradoService.getVariables(SessionId, id), HttpStatus.OK);
    }

    @Operation(summary = "Resumen de documentos generados por plantilla",
            description = "Totales (éxito/error, doc/web, IA, tokens, variables sin valor, tiempos promedio) y agrupaciones por documento, plantilla, instancia y usuario")
    @PostMapping("/resumen")
    public ResponseEntity<ResponseResumenDocPlantilla> getResumen(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDocumentoPlantillaGenerado inputData) {

        return new ResponseEntity<>(documentoPlantillaGeneradoService.getResumen(SessionId, inputData), HttpStatus.OK);
    }
}
