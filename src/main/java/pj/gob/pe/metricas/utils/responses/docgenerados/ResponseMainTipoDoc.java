package pj.gob.pe.metricas.utils.responses.docgenerados;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseMainTipoDoc {

    private Long idTipoDocumento;
    private String tipoDocumento;

    private Long totalWeb;
    private Long totalDoc;

    private List<ResponseMainDoc> documentos;
}
