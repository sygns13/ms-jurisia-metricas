package pj.gob.pe.metricas.utils.responses.consultaia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "Output Consulta IA Security External")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputConsultaIAExternal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer tipoDocumento;
    private String documento;
    private String apellidos;
    private String nombres;
    private String cargo;
    private String username;
    private String email;
    private Integer genero;
    private String telefono;
    private String direccion;
    private Integer activo;
}
