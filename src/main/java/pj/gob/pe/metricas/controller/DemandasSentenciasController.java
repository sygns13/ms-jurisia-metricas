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
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.service.business.DemandasSentenciasService;
import pj.gob.pe.metricas.utils.inputs.demandassentencias.InputDemandasSentencias;
import pj.gob.pe.metricas.utils.responses.demandassentencias.ResponseTotalDemandasSentencias;

@Tag(name = "Service Metrics Controller", description = "Endpoints de servicio de métricas para Demandas Sentencias")
@RestController
@RequestMapping("/v1/demandas-sentencias")
@RequiredArgsConstructor
public class DemandasSentenciasController {

    private final DemandasSentenciasService demandasSentenciasService;

    @Operation(summary = "Obtencion de Reporte de Demandas Sentencias", description = "Obtencion de Reporte de Sentencias de Demandas generadas")
    @PostMapping("/get-data")
    public ResponseEntity<Page<DemandasSentenciasToKafka>> reportDemandasSentencias(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDemandasSentencias inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.ASC, "id");

        Page<DemandasSentenciasToKafka> dataResponse = demandasSentenciasService.getDemandasSentencias(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<Page<DemandasSentenciasToKafka>>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Total de Demandas Sentencias", description = "Get Total de Sentencias de Demandas generadas")
    @GetMapping("/gettotaloperaciones")
    public ResponseEntity<ResponseTotalDemandasSentencias> listTotal(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        String buscar = "";

        Long totalDemandas = demandasSentenciasService.getTotalDemandasSentencias(buscar, SessionId);

        if(totalDemandas == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        ResponseTotalDemandasSentencias responseTotalDemandas = new ResponseTotalDemandasSentencias();

        responseTotalDemandas.setTotalDemandasSentencias(totalDemandas);

        return new ResponseEntity<>(responseTotalDemandas, HttpStatus.OK);
    }
}
