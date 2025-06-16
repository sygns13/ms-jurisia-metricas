package pj.gob.pe.metricas.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response Total Documentos Generados Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTotalDocGenerados {

    Long totalDocsGenerados;
}
