package pj.gob.pe.metricas.utils.inputs.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros del reporte admin de documentos generados por plantilla agrupados por instancia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputAdminDocPlantillaPorInstancia {

    @NotNull(message = "fechaInicial es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha inicial (inclusive) a comparar contra regDate. Formato yyyy-MM-dd", example = "2026-01-01")
    private LocalDate fechaInicial;

    @NotNull(message = "fechaFinal es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha final (inclusive) a comparar contra regDate. Formato yyyy-MM-dd", example = "2026-12-31")
    private LocalDate fechaFinal;

    @Schema(description = "Código de sede SIJ (opcional, filtro exacto)", example = "0201")
    private String codSede;

    @Schema(description = "Código de instancia SIJ (opcional, filtro exacto)", example = "702")
    private String codInstancia;
}
