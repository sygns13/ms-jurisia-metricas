package pj.gob.pe.metricas.utils.inputs.demandasadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros del reporte admin de calificaciones/sentencias agrupadas por expediente y usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputAdminDemandasPorExpediente {

    @NotNull(message = "fechaInicial es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha inicial (inclusive) a comparar contra fechaSend. Formato yyyy-MM-dd", example = "2026-01-01")
    private LocalDate fechaInicial;

    @NotNull(message = "fechaFinal es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha final (inclusive) a comparar contra fechaSend. Formato yyyy-MM-dd", example = "2026-12-31")
    private LocalDate fechaFinal;

    @NotBlank(message = "codSede es requerido")
    @Schema(description = "Código de sede SIJ (filtro exacto)", example = "0201")
    private String codSede;

    @NotBlank(message = "cinstancia es requerida")
    @Schema(description = "Código de instancia SIJ (filtro exacto)", example = "301")
    private String cinstancia;

    @Schema(description = "ID del usuario que realizó la operación (opcional, filtro exacto)", example = "1")
    private Long idUser;

    @Schema(description = "Número de expediente (opcional, filtro tipo LIKE)", example = "00272")
    private String expNro;

    @Schema(description = "Año del expediente (opcional, filtro exacto)", example = "2026")
    private String anio;
}
