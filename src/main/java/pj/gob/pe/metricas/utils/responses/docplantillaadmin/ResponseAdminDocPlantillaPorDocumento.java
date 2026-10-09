package pj.gob.pe.metricas.utils.responses.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "Fila del reporte admin de documentos generados por plantilla agrupados por tipo de documento y documento")
@JsonPropertyOrder({"codSede", "sede", "codInstancia", "instancia", "idTipoDocumento", "tipoDocumento", "idDocumento",
        "documento", "totalMaterias", "materias", "totalUsuarios", "usuarios", "totalDocumentos", "anios",
        "totalExpedientes", "expedientes"})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResponseAdminDocPlantillaPorDocumento extends ResponseAdminDocPlantillaMetricas {

    public static final String DOCUMENTO_TODOS = "TODOS";

    @Schema(description = "Código de sede SIJ", example = "0201")
    private String codSede;

    @Schema(description = "Descripción de la sede", example = "Sede Central de Corte")
    private String sede;

    @Schema(description = "Código de instancia SIJ", example = "702")
    private String codInstancia;

    @Schema(description = "Nombre de la instancia")
    private String instancia;

    @Schema(description = "ID del tipo de documento", example = "5")
    private Long idTipoDocumento;

    @Schema(description = "Tipo de documento", example = "AUTO")
    private String tipoDocumento;

    @Schema(description = "ID del documento; null cuando no se filtró por documento (agrupado por tipo)", example = "16")
    private Long idDocumento;

    @Schema(description = "Documento; TODOS cuando no se filtró por documento (agrupado por tipo)", example = "AUTO ADMISORIO")
    private String documento;

    @Schema(description = "Materias distintas de los expedientes")
    private Long totalMaterias;

    @Schema(description = "Materias, separadas por coma")
    private String materias;

    @Schema(description = "Usuarios distintos que generaron el documento")
    private Long totalUsuarios;

    @Schema(description = "Usuarios (username), separados por coma")
    private String usuarios;

    @Schema(description = "Total de documentos generados del tipo de documento / documento")
    private Long totalDocumentos;

    @Schema(description = "Años de los expedientes, separados por coma")
    private String anios;

    @Schema(description = "Expedientes distintos (nUnico)")
    private Long totalExpedientes;

    @Schema(description = "Expedientes (formateados), separados por coma")
    private String expedientes;
}
