package pj.gob.pe.metricas.utils.inputs.demandasadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros del reporte admin de calificaciones/sentencias agrupadas por instancia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputAdminDemandasPorInstancia {

    @NotNull(message = "fechaInicial es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha inicial (inclusive) a comparar contra fechaSend. Formato yyyy-MM-dd", example = "2026-01-01")
    private LocalDate fechaInicial;

    @NotNull(message = "fechaFinal es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha final (inclusive) a comparar contra fechaSend. Formato yyyy-MM-dd", example = "2026-12-31")
    private LocalDate fechaFinal;

    @Schema(description = "Código de sede SIJ (opcional, filtro exacto)", example = "0201")
    private String codSede;

    @Schema(description = "Código de instancia SIJ (opcional, filtro exacto)", example = "301")
    private String cinstancia;
}
