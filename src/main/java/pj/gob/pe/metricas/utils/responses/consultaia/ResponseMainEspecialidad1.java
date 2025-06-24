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
public class ResponseMainEspecialidad1 {

    private String codEspecialidad;
    private String especialidad;
    private Long consultas;
}
