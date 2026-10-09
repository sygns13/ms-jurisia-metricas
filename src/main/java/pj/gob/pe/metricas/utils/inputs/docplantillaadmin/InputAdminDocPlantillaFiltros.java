package pj.gob.pe.metricas.utils.inputs.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros de los reportes admin de documentos generados por plantilla agrupados por expediente o por tipo de documento/documento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputAdminDocPlantillaFiltros {

    @NotNull(message = "fechaInicial es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha inicial (inclusive) a comparar contra regDate. Formato yyyy-MM-dd", example = "2026-01-01")
    private LocalDate fechaInicial;

    @NotNull(message = "fechaFinal es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha final (inclusive) a comparar contra regDate. Formato yyyy-MM-dd", example = "2026-12-31")
    private LocalDate fechaFinal;

    @NotBlank(message = "codSede es requerido")
    @Schema(description = "Código de sede SIJ (filtro exacto)", example = "0201")
    private String codSede;

    @NotBlank(message = "codInstancia es requerida")
    @Schema(description = "Código de instancia SIJ (filtro exacto)", example = "702")
    private String codInstancia;

    @Schema(description = "ID del usuario que generó el documento (opcional, filtro exacto)", example = "1")
    private Long idUser;

    @Schema(description = "Número de expediente (opcional, filtro tipo LIKE sobre codNumero)", example = "00012")
    private String expNro;

    @Schema(description = "Año del expediente (opcional, filtro exacto sobre codYear)", example = "2025")
    private String anio;

    @Schema(description = "ID del tipo de documento (opcional, filtro exacto)", example = "5")
    private Long idTipoDocumento;

    @Schema(description = "ID del documento (opcional, filtro exacto)", example = "16")
    private Long idDocumento;
}
