package pj.gob.pe.metricas.utils.responses.docgenerados;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMainSumarisimo {

    private String codInstancia;
    private String instancia;
    private String juez;

    private Long totalWeb;
    private Long totalDoc;

    private List<ResponseMainEspecialidad> especialidades;
}
