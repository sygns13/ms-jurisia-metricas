package pj.gob.pe.metricas.utils.inputs.docgenerados;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Input")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMainDocGenerado {

    @NotNull( message = "{input.instancia.notnull}")
    @Size(min = 1, max = 20, message = "{input.instancia.size}")
    private String codInstancia;
    private String juez;
    private String codEspecialidad;
    private Long idDocumento;
    private Long idTipoDocumento;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha Fin de Consulta")
    private LocalDate fechaFin;


}
