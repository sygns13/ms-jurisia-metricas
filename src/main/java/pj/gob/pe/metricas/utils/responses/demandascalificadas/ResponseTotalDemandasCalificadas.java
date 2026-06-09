package pj.gob.pe.metricas.utils.responses.demandascalificadas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response Total Demandas Calificadas Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTotalDemandasCalificadas {

    private Long totalDemandasCalificadas;
}
