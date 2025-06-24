package pj.gob.pe.metricas.utils.inputs.consultaia;

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
public class InputConsultaIAMain1 {

    @NotNull( message = "{input.instancia.notnull}")
    @Size(min = 1, max = 20, message = "{input.instancia.size}")
    private String codInstancia;
    private Long idUser;
    private String codEspecialidad;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha Fin de Consulta")
    private LocalDate fechaFin;
}
