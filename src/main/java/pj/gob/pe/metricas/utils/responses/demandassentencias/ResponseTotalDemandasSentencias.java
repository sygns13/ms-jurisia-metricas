package pj.gob.pe.metricas.utils.responses.demandassentencias;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response Total Demandas Sentencias Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTotalDemandasSentencias {

    private Long totalDemandasSentencias;
}
