package pj.gob.pe.metricas.utils.inputs.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros del detalle admin de documentos generados por plantilla (filas de CabDocumentoPlantillaGenerado)")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputAdminDocPlantillaDetalle {

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
