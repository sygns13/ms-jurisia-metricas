package pj.gob.pe.metricas.utils.responses.consultaia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseConsultaIAMain2 {

    private String codInstancia;
    private String instancia;

    private Long consultas;
}
