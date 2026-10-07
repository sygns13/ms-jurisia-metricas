package pj.gob.pe.metricas.utils.responses.docplantilla;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Resumen de documentos generados por plantilla para los filtros indicados")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResumenDocPlantilla {

    // ---- Volumen ----
    private Long totalGenerados;
    private Long totalExitosos;
    private Long totalErrores;
    private Long totalDoc;
    private Long totalWeb;

    // ---- IA ----
    private Long iaExitoso;
    private Long iaError;
    private Long iaNoAplica;
    private Long parrafosEnviadosIA;
    private Long parrafosCorregidosIA;
    private Long bloquesSolicitadosIA;
    private Long bloquesGeneradosIA;
    private Long promptTokens;
    private Long candidatesTokens;
    private Long thoughtsTokens;
    private Long totalTokens;

    // ---- Variables ----
    private Long totalVariables;
    private Long variablesSinValor;

    // ---- Tiempos (solo generaciones exitosas) ----
    private Double promedioTiempoTotalMs;
    private Double promedioTiempoSijMs;
    private Double promedioTiempoIAMs;

    // ---- Agrupaciones ----
    private List<ResponseResumenItemDocPlantilla> porDocumento;
    private List<ResponseResumenItemDocPlantilla> porPlantilla;
    private List<ResponseResumenItemDocPlantilla> porInstancia;
    private List<ResponseResumenItemDocPlantilla> porUsuario;
}
