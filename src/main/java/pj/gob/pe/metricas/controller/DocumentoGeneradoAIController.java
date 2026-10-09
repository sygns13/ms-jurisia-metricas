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
import pj.gob.pe.metricas.exception.ModeloNotFoundException;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoPlantillaService;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputMainDocGenerado;
import pj.gob.pe.metricas.utils.inputs.docgenerados.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseDocumentoGeneradoPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseMainSumarisimoPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseTotalDocGeneradosPlantilla;
import pj.gob.pe.metricas.utils.responses.docgeneradosplantilla.ResponseTotalFiltersDocGeneradosPlantilla;

/**
 * Reportes de documentos generados. Desde la versión por plantilla la fuente es
 * CabDocumentoPlantillaGenerado (el histórico de CabDocumentoGenerado se migró a esa tabla); los
 * responses conservan el contrato anterior y agregan datos del flujo por plantilla.
 */
@Tag(name = "Service Metrics Controller", description = "Endpoints de servicio de métricas para Documento Generado IA")
@RestController
@RequestMapping("/v1/documento-generado-ia")
@RequiredArgsConstructor
public class DocumentoGeneradoAIController {

    private final DocumentoGeneradoPlantillaService documentoGeneradoPlantillaService;

    @Operation(summary = "Obtencion de Reporte de  Documento Generado IA", description = "Obtencion de Reporte de  Documento Generado IA (CabDocumentoPlantillaGenerado), en orden cronológico")
    @PostMapping("/get-data")
    public ResponseEntity<Page<ResponseDocumentoGeneradoPlantilla>> reportCabDocumentoGenerado(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDocumentoGeneradoIA inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        // Orden cronológico: los registros migrados del flujo anterior tienen ids posteriores a
        // los primeros generados por plantilla, por eso se ordena por fecha y luego por id.
        Pageable pageable = PageRequest.of(page,size).withSort(Sort.by(Sort.Direction.ASC, "regDatetime", "id"));

        Page<ResponseDocumentoGeneradoPlantilla> dataResponse = documentoGeneradoPlantillaService.getDocumentosGenerados(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Total de  Documento Generado IA", description = "Total de documentos generados con éxito por el usuario de la sesión")
    @GetMapping("/gettotaloperaciones")
    public ResponseEntity<ResponseTotalDocGeneradosPlantilla> listTotal(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        ResponseTotalDocGeneradosPlantilla responseTotal = documentoGeneradoPlantillaService.getTotalDocGenerados(SessionId);

        if(responseTotal == null || responseTotal.getTotalDocsGenerados() == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<>(responseTotal, HttpStatus.OK);
    }

    @Operation(summary = "Get Totales de  Documento Generado IA por filters", description = "Get Totales de  Documento Generado IA por filters")
    @PostMapping("/gettotaloperaciones-filters")
    public ResponseEntity<ResponseTotalFiltersDocGeneradosPlantilla> listTotalFilters(
            @RequestHeader("SessionId") String SessionId,
            @Valid@RequestBody InputTotalesCabDocGenerado inputData) throws Exception{

        ResponseTotalFiltersDocGeneradosPlantilla responseTotalFilters = documentoGeneradoPlantillaService.getTotalDocGeneradosFilters(inputData, SessionId);

        if(responseTotalFilters == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<>(responseTotalFilters, HttpStatus.OK);
    }

    @Operation(summary = "Get Main Documentos generados", description = "Get Main Documentos generados")
    @PostMapping("/main")
    public ResponseEntity<ResponseMainSumarisimoPlantilla> getMainReportDocGenerados(
            @RequestHeader("SessionId") String SessionId,
            @Valid@RequestBody InputMainDocGenerado inputData) throws Exception{

        ResponseMainSumarisimoPlantilla responseMain = documentoGeneradoPlantillaService.getMainDocumentoGenerado(inputData, SessionId);

        if(responseMain == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<>(responseMain, HttpStatus.OK);
    }
}
