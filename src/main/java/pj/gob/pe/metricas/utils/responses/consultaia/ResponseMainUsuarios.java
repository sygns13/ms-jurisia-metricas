package pj.gob.pe.metricas.utils.responses.consultaia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseMainUsuarios {

    private Long idUser;
    private String documento;
    private String nombres;
    private String apellidos;
    private String username;
    private String cargoUsuario;

    private Long consultas;

    private List<ResponseMainEspecialidad1> especialidades;

}
