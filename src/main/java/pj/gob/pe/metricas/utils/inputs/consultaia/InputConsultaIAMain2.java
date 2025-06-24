package pj.gob.pe.metricas.utils.inputs.consultaia;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class InputConsultaIAMain2 {

    private String codInstancia;
    private Long idUser;
    private String codEspecialidad;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha Fin de Consulta")
    private LocalDate fechaFin;
}
