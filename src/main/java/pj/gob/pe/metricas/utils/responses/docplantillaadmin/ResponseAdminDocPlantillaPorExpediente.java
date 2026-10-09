package pj.gob.pe.metricas.utils.responses.docplantillaadmin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "Fila del reporte admin de documentos generados por plantilla agrupados por expediente y usuario")
@JsonPropertyOrder({"codSede", "sede", "codInstancia", "instancia", "nunico", "xformato", "codMateria", "materia",
        "idUser", "username", "nombreUsuario", "cargo", "dependencia", "totalDocumentos", "anio", "expNro",
        "numIncidente", "codEspecialidad", "especialidad", "ubicacion", "estado", "juez", "especialista",
        "demandante", "demandado", "totalTiposDocumento", "totalDocumentosDistintos", "documentosGenerados",
        "idUltimoRegistro"})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResponseAdminDocPlantillaPorExpediente extends ResponseAdminDocPlantillaMetricas {

    @Schema(description = "Código de sede SIJ", example = "0201")
    private String codSede;

    @Schema(description = "Descripción de la sede", example = "Sede Central de Corte")
    private String sede;

    @Schema(description = "Código de instancia SIJ", example = "702")
    private String codInstancia;

    @Schema(description = "Nombre de la instancia")
    private String instancia;

    @Schema(description = "ID del expediente (número único)", example = "2025000120201133")
    private Long nUnico;

    @Schema(description = "Expediente formateado", example = "00012-2025-0-0201-JP-FC-02")
    private String xFormato;

    @Schema(description = "Código de materia", example = "247")
    private String codMateria;

    @Schema(description = "Descripción de la materia", example = "EXONERACION DE ALIMENTOS")
    private String materia;

    @Schema(description = "ID del usuario que generó los documentos")
    private Long idUser;

    private String username;
    private String nombreUsuario;
    private String cargo;
    private String dependencia;

    @Schema(description = "Total de documentos generados por el usuario para el expediente")
    private Long totalDocumentos;

    @Schema(description = "Año del expediente", example = "2025")
    private String anio;

    @Schema(description = "Número de expediente", example = "00012")
    private String expNro;

    private String numIncidente;
    private String codEspecialidad;
    private String especialidad;
    private String ubicacion;
    private String estado;
    private String juez;
    private String especialista;
    private String demandante;
    private String demandado;

    @Schema(description = "Tipos de documento distintos generados")
    private Long totalTiposDocumento;

    @Schema(description = "Documentos distintos generados")
    private Long totalDocumentosDistintos;

    @Schema(description = "Documentos generados (TIPO - DOCUMENTO), separados por coma", example = "AUTO - AUTO ADMISORIO")
    private String documentosGenerados;

    @Schema(description = "ID del último registro (generación más reciente) de CabDocumentoPlantillaGenerado")
    private Long idUltimoRegistro;
}
