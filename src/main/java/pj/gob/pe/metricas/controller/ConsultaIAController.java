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
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.service.business.ConsultaIAService;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIA;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputDetailConsultaIA;

@Tag(name = "Service Metrics Controller", description = "Endpoints de servicio de métricas para Consulta IA")
@RestController
@RequestMapping("/v1/consulta-ia")
@RequiredArgsConstructor
public class ConsultaIAController {

    private final ConsultaIAService consultaIAService;

    @Operation(summary = "Obtencion de Reporte de Cabeceras de ConsultaIA", description = "Obtencion de Reporte de Cabeceras de ConsultaIA")
    @PostMapping("/cabeceras")
    public ResponseEntity<Page<CabConsultaIA>> reportCabConsultaIA(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputConsultaIA inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.ASC, "id");

        Page<CabConsultaIA> dataResponse = consultaIAService.getGeneralConsultaIA(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<Page<CabConsultaIA>>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Obtencion de Reporte de Detallles de ConsultaIA", description = "Obtencion de Reporte de Detalles de ConsultaIA")
    @PostMapping("/detalles")
    public ResponseEntity<Page<DetailConsultaIA>> reportDetailConsultaIA(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDetailConsultaIA inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.ASC, "id");

        Page<DetailConsultaIA> dataResponse = consultaIAService.getDetailConsultaIA(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<Page<DetailConsultaIA>>(dataResponse, HttpStatus.OK);
    }
}
