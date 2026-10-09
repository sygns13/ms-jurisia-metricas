package pj.gob.pe.metricas.utils.responses.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "Fila del reporte admin de documentos generados por plantilla agrupados por sede e instancia")
@JsonPropertyOrder({"codSede", "sede", "codInstancia", "instancia", "totalUsuarios", "totalExpedientes", "totalDocumentos",
        "totalTiposDocumento", "totalDocumentosDistintos", "totalMaterias"})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResponseAdminDocPlantillaPorInstancia extends ResponseAdminDocPlantillaMetricas {

    @Schema(description = "Código de sede SIJ", example = "0201")
    private String codSede;

    @Schema(description = "Descripción de la sede", example = "Sede Central de Corte")
    private String sede;

    @Schema(description = "Código de instancia SIJ", example = "702")
    private String codInstancia;

    @Schema(description = "Nombre de la instancia", example = "2° JUZGADO PAZ LETRADO - Sede Central")
    private String instancia;

    @Schema(description = "Usuarios distintos que generaron documentos en la instancia")
    private Long totalUsuarios;

    @Schema(description = "Expedientes distintos (nUnico) usados para generar documentos")
    private Long totalExpedientes;

    @Schema(description = "Total de documentos generados (todas las generaciones)")
    private Long totalDocumentos;

    @Schema(description = "Tipos de documento distintos generados")
    private Long totalTiposDocumento;

    @Schema(description = "Documentos distintos generados")
    private Long totalDocumentosDistintos;

    @Schema(description = "Materias distintas de los expedientes")
    private Long totalMaterias;
}
