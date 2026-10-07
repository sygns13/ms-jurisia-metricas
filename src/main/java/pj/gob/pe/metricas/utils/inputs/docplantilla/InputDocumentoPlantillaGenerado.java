package pj.gob.pe.metricas.utils.inputs.docplantilla;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Filtros de reportes de Documentos generados por plantilla")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDocumentoPlantillaGenerado {

    // ---- Usuario ----
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

    // ---- Documento generado ----
    @Schema(description = "doc o web")
    private String typedoc;

    @Schema(description = "1 exitoso, 2 error")
    private Integer status;

    @Schema(description = "NO_APLICA, EXITOSO o ERROR")
    private String estadoIA;

    private String codSede;
    private String codInstancia;
    private String codEspecialidad;
    private String codMateria;
    private String numeroExpediente;
    private String yearExpediente;
    private Long numUnico;
    private String xdeFormato;
    private String dniDemandante;
    private String dniDemandado;
    private Long idTipoDocumento;
    private Long idDocumento;
    private Long idPlantilla;
    private String codigoPlantilla;
    private Integer versionPlantilla;
    private String model;

    @Schema(description = "Fecha de Inicio de Consulta")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha Fin de Consulta")
    private LocalDate fechaFin;
}
