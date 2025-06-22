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
public class ResponseMainEspecialidad {

    private String codEspecialidad;
    private String especialidad;

    private Long totalWeb;
    private Long totalDoc;

    private List<ResponseMainTipoDoc> tipoDocumentos;
}
