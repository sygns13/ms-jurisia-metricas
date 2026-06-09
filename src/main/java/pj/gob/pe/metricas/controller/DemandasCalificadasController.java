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
import pj.gob.pe.metricas.model.entities.DemandasCalificadasToKafka;
import pj.gob.pe.metricas.service.business.DemandasCalificadasService;
import pj.gob.pe.metricas.utils.inputs.demandascalificadas.InputDemandasCalificadas;
import pj.gob.pe.metricas.utils.responses.demandascalificadas.ResponseTotalDemandasCalificadas;

@Tag(name = "Service Metrics Controller", description = "Endpoints de servicio de métricas para Demandas Calificadas")
@RestController
@RequestMapping("/v1/demandas-calificadas")
@RequiredArgsConstructor
public class DemandasCalificadasController {

    private final DemandasCalificadasService demandasCalificadasService;

    @Operation(summary = "Obtencion de Reporte de Demandas Calificadas", description = "Obtencion de Reporte de Demandas Calificadas")
    @PostMapping("/get-data")
    public ResponseEntity<Page<DemandasCalificadasToKafka>> reportDemandasCalificadas(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody InputDemandasCalificadas inputData,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws Exception{

        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.ASC, "id");

        Page<DemandasCalificadasToKafka> dataResponse = demandasCalificadasService.getDemandasCalificadas(SessionId, inputData, pageable);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<Page<DemandasCalificadasToKafka>>(dataResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Total de Demandas Calificadas", description = "Get Total de Demandas Calificadas")
    @GetMapping("/gettotaloperaciones")
    public ResponseEntity<ResponseTotalDemandasCalificadas> listTotal(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        String buscar = "";

        Long totalDemandas = demandasCalificadasService.getTotalDemandasCalificadas(buscar, SessionId);

        if(totalDemandas == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        ResponseTotalDemandasCalificadas responseTotalDemandas = new ResponseTotalDemandasCalificadas();

        responseTotalDemandas.setTotalDemandasCalificadas(totalDemandas);

        return new ResponseEntity<>(responseTotalDemandas, HttpStatus.OK);
    }
}
