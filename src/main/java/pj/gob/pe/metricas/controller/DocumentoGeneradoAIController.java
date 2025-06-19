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
import pj.gob.pe.metricas.model.entities.CabDocumentoGenerado;
import pj.gob.pe.metricas.service.business.DocumentoGeneradoService;
import pj.gob.pe.metricas.utils.InputDocumentoGeneradoIA;
import pj.gob.pe.metricas.utils.InputTotalesCabDocGenerado;
import pj.gob.pe.metricas.utils.ResponseTotalDocGenerados;
import pj.gob.pe.metricas.utils.ResponseTotalFiltersDocGenerados;

@Tag(name = "Service Metrics Controller", description = "Endpoints de servicio de métricas para Documento Generado IA")
@RestController
@RequestMapping("/v1/documento-generado-ia")
@RequiredArgsConstructor
public class DocumentoGeneradoAIController {
    
    private final DocumentoGeneradoService documentoGeneradoService;

    @Operation(summary = "Obtencion de Reporte de  Documento Generado IA", description = "Obtencion de Reporte de  Documento Generado IA")
    @PostMapping("/get-data")
    public ResponseEntity<Page<CabDocumentoGenerado>> reportCabDocumentoGenerado(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDocumentoGeneradoIA inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.ASC, "id");

        Page<CabDocumentoGenerado> dataResponse = documentoGeneradoService.getDocumentosGenerados(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<Page<CabDocumentoGenerado>>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Total de  Documento Generado IA", description = "Get de Documento Generado IA")
    @GetMapping("/gettotaloperaciones")
    public ResponseEntity<ResponseTotalDocGenerados> listTotal(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        String buscar = "";

        Long totalConversaciones = documentoGeneradoService.getTotalDocGenerados(buscar, SessionId);

        if(totalConversaciones == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        ResponseTotalDocGenerados responseTotalConversaciones = new ResponseTotalDocGenerados();

        responseTotalConversaciones.setTotalDocsGenerados(totalConversaciones);

        return new ResponseEntity<>(responseTotalConversaciones, HttpStatus.OK);
    }

    @Operation(summary = "Get Totales de  Documento Generado IA por filters", description = "Get Totales de  Documento Generado IA por filters")
    @PostMapping("/gettotaloperaciones-filters")
    public ResponseEntity<ResponseTotalFiltersDocGenerados> listTotalFilters(
            @RequestHeader("SessionId") String SessionId,
            @Valid@RequestBody InputTotalesCabDocGenerado inputData) throws Exception{

        String buscar = "";

        ResponseTotalFiltersDocGenerados responseTotalConversacionesFilters = documentoGeneradoService.getTotalDocGeneradosFilters(inputData, SessionId);

        if(responseTotalConversacionesFilters == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }


        return new ResponseEntity<>(responseTotalConversacionesFilters, HttpStatus.OK);
    }
}
