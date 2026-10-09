package pj.gob.pe.metricas.utils.responses.docgeneradosplantilla;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pj.gob.pe.metricas.utils.responses.docgenerados.ResponseMainEspecialidad;

/**
 * Mismo contrato que {@link ResponseMainEspecialidad} más datos adicionales del flujo por plantilla.
 */
@Schema(description = "Resumen main por especialidad (flujo por plantilla)")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResponseMainEspecialidadPlantilla extends ResponseMainEspecialidad implements ResponseMainNodo {

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
}
