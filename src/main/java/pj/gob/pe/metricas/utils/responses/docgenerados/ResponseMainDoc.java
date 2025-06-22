package pj.gob.pe.metricas.utils.responses.docgenerados;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseMainDoc {

    private Long idDocumento;
    private String documento;

    private Long totalWeb;
    private Long totalDoc;
}
