package pj.gob.pe.metricas.utils.responses.docplantilla;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Agrupación del resumen de documentos generados por plantilla")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResumenItemDocPlantilla {

    @Schema(description = "Código del grupo (idDocumento, codInstancia, userId o codigoPlantilla)")
    private String codigo;

    @Schema(description = "Descripción del grupo")
    private String descripcion;

    private Long total;
    private Long exitosos;
    private Long errores;
    private Long totalDoc;
    private Long totalWeb;
    private Long totalTokens;
    private Long variablesSinValor;
    private Double promedioTiempoTotalMs;
}
