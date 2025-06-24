package pj.gob.pe.metricas.utils.responses.consultaia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMainUsuarios {

    private String documento;
    private String nombres;
    private String apellidos;
    private String username;
    private String cargoUsuario;

    private Long consultas;

    private List<ResponseMainEspecialidad1> especialidades;

}
