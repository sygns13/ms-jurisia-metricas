package pj.gob.pe.metricas.utils.responses.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Métricas comunes a todas las agrupaciones del reporte admin de documentos generados por plantilla.
 * Los tiempos promedio/mínimo/máximo se calculan solo sobre generaciones exitosas (status 1).
 */
@Data
@NoArgsConstructor
public abstract class ResponseAdminDocPlantillaMetricas {

    @Schema(description = "Generaciones exitosas (status 1)")
    private Long totalExitosos;

    @Schema(description = "Generaciones con error (status 2)")
    private Long totalErrores;

    @Schema(description = "Generaciones iniciadas sin finalizar (status 0)")
    private Long totalEnProceso;

    @Schema(description = "Porcentaje de generaciones exitosas sobre el total", example = "92.5")
    private Double porcentajeExito;

    @Schema(description = "Generaciones descargadas como Word (typedoc = doc)")
    private Long totalDoc;

    @Schema(description = "Generaciones visualizadas en web (typedoc = web)")
    private Long totalWeb;

    @Schema(description = "Generaciones con plantilla configurada para corrección IA (corregirIA = 1)")
    private Long totalConCorreccionIA;

    @Schema(description = "Generaciones con IA exitosa (estadoIA = EXITOSO)")
    private Long iaExitoso;

    @Schema(description = "Generaciones con error de IA (estadoIA = ERROR)")
    private Long iaError;

    @Schema(description = "Generaciones sin IA (estadoIA = NO_APLICA)")
    private Long iaNoAplica;

    private Long parrafosEnviadosIA;
    private Long parrafosCorregidosIA;
    private Long bloquesSolicitadosIA;
    private Long bloquesGeneradosIA;

    private Long promptTokens;
    private Long candidatesTokens;
    private Long thoughtsTokens;
    private Long cachedTokens;
    private Long totalTokens;

    @Schema(description = "Variables de plantilla procesadas (suma)")
    private Long totalVariables;
    private Long variablesSij;
    private Long variablesCalculadas;
    private Long variablesManuales;
    private Long variablesIA;
    private Long variablesNoDefinidas;

    @Schema(description = "Variables que quedaron sin valor (suma)")
    private Long variablesSinValor;

    private Long reemplazosRealizados;

    @Schema(description = "Tiempo total promedio en ms (solo exitosos)")
    private Double tiempoPromedioTotalMs;

    @Schema(description = "Tiempo total mínimo en ms (solo exitosos)")
    private Long tiempoMinimoTotalMs;

    @Schema(description = "Tiempo total máximo en ms (solo exitosos)")
    private Long tiempoMaximoTotalMs;

    @Schema(description = "Tiempo promedio de consulta al SIJ en ms (solo exitosos)")
    private Double tiempoPromedioSijMs;

    @Schema(description = "Tiempo promedio de IA en ms (solo exitosos que usaron IA)")
    private Double tiempoPromedioIAMs;

    @Schema(description = "Tamaño promedio del documento generado en bytes")
    private Double tamanioPromedioSalidaBytes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de la primera generación del periodo")
    private LocalDateTime fechaPrimeraGeneracion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de la última generación del periodo")
    private LocalDateTime fechaUltimaGeneracion;

    @Schema(description = "Plantillas utilizadas (codigo vN), separadas por coma", example = "template_auto_01_doc16 v2")
    private String plantillasUtilizadas;

    @Schema(description = "Modelos de IA utilizados, separados por coma", example = "gemini-3.1-pro-preview")
    private String modelosUtilizados;
}
