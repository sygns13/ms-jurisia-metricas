package pj.gob.pe.metricas.utils.inputs.demandascalificadas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Input Demandas Calificadas Filters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDemandasCalificadas {

    //Seccion Usuario
    @Schema(description = "ID de Usuario")
    private Long idUser;

    @Schema(description = "Documento de Identidad de Usuario")
    private String documento;

    @Schema(description = "Apellidos de Usuario")
    private String apellidos;

    @Schema(description = "Nombres de Usuario")
    private String nombres;

    @Schema(description = "Cargo de Usuario")
    private String cargo;

    @Schema(description = "Username de Usuario")
    private String username;

    @Schema(description = "Email de Usuario")
    private String email;

    //Seccion Demanda Calificada
    private Long nUnico;
    private String model;
    private Integer status;
    private String anio;
    private String expNro;
    private String tipoExpediente;
    private String cinstancia;
    private String cespecialidad;
    private String cmateria;
    private String cubicacion;
    private String xdescEstado;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha Fin de Consulta")
    private LocalDate fechaFin;
}
