package pj.gob.pe.metricas.utils.responses.docgeneradosplantilla;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseTotalFiltersDocGenerados;

/**
 * Mismo contrato que {@link ResponseTotalFiltersDocGenerados} más datos adicionales del flujo por plantilla.
 */
@Schema(description = "Totales por filtros de documentos generados (flujo por plantilla)")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResponseTotalFiltersDocGeneradosPlantilla extends ResponseTotalFiltersDocGenerados implements MetricasPlantillaExtra {

    // ===== Datos adicionales del flujo por plantilla =====
    @Schema(description = "Generaciones con error (status 2); no se incluyen en los totales doc/web")
    private Long totalErrores;

    @Schema(description = "Generaciones exitosas con corrección IA exitosa (estadoIA = EXITOSO)")
    private Long iaExitoso;

    @Schema(description = "Generaciones exitosas con error de IA (estadoIA = ERROR)")
    private Long iaError;

    @Schema(description = "Tokens de Gemini consumidos por las generaciones exitosas")
    private Long totalTokens;

    @Schema(description = "Tiempo total promedio en ms de las generaciones exitosas")
    private Double tiempoPromedioTotalMs;

    @Schema(description = "Generaciones exitosas descargadas en Word")
    private Long totalDoc;

    @Schema(description = "Generaciones exitosas vistas en web")
    private Long totalWeb;

    @Schema(description = "Generaciones exitosas sin IA (estadoIA = NO_APLICA)")
    private Long iaNoAplica;

    @Schema(description = "Variables que quedaron sin valor en las generaciones exitosas")
    private Long variablesSinValor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Primera generación exitosa del periodo")
    private LocalDateTime fechaPrimeraGeneracion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Última generación exitosa del periodo")
    private LocalDateTime fechaUltimaGeneracion;
}
