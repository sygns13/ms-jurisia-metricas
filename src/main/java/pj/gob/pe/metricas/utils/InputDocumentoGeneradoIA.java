package pj.gob.pe.metricas.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Input Documento Generado Filters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDocumentoGeneradoIA {

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

    //Seccion Documento Generado
    private String typedoc;
    private String codSede;
    private String codInstancia;
    private String codEspecialidad;
    private String codMateria;
    private String numeroExpediente;
    private String yearExpediente;
    private Long idDocumento;
    private Long idTipoDocumento;
    private Long numUnico;
    private String xdeFormato;
    private String dniDemandante;
    private String dniDemandado;
    private String templateCode;
    private Long templateID;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha F de Consulta")
    private LocalDate fechaFin;
}
