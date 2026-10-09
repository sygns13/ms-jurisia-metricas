package pj.gob.pe.metricas.utils.responses.demandasadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Fila del reporte admin de calificaciones/sentencias agrupadas por expediente y usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminDemandasPorExpediente {

    @Schema(description = "Código de sede SIJ", example = "0201")
    private String codSede;

    @Schema(description = "Descripción de la sede SIJ", example = "Sede Central de Corte")
    private String sede;

    @Schema(description = "Código de instancia SIJ", example = "301")
    private String cinstancia;

    @Schema(description = "Nombre de la instancia", example = "1° JUZGADO FAMILIA - Sede San Martin")
    private String xnomInstancia;

    @Schema(description = "ID del expediente (número único)", example = "2026002720201133")
    private Long nUnico;

    @Schema(description = "Expediente formateado", example = "00272-2026-0-0201-JR-FC-01")
    private String xformato;

    @Schema(description = "Código de materia", example = "637")
    private String cmateria;

    @Schema(description = "Descripción de la materia", example = "TENENCIA")
    private String xdescMateria;

    @Schema(description = "Tipo de expediente", example = "Digital")
    private String tipoExpediente;

    @Schema(description = "ID del usuario que realizó la operación", example = "1")
    private Long idUser;

    @Schema(description = "Username del usuario")
    private String username;

    @Schema(description = "Nombres del usuario")
    private String nombres;

    @Schema(description = "Apellidos del usuario")
    private String apellidos;

    @Schema(description = "Documento de identidad del usuario")
    private String documento;

    @Schema(description = "Cargo del usuario")
    private String cargo;

    @Schema(description = "Dependencia del usuario")
    private String nombreDependencia;

    @Schema(description = "Total de calificaciones o sentencias del usuario en el expediente (todas las versiones)")
    private Long totalRegistros;

    @Schema(description = "Año del expediente", example = "2026")
    private String anio;

    @Schema(description = "Número de expediente", example = "00272")
    private String expNro;

    @Schema(description = "Número de incidente", example = "0")
    private String nincidente;

    @Schema(description = "Código de especialidad", example = "FC")
    private String cespecialidad;

    @Schema(description = "Ubicación del expediente", example = "MPU / CDG")
    private String xdescUbicacion;

    @Schema(description = "Estado del expediente", example = "EN CALIFICACION")
    private String xdescEstado;

    @Schema(description = "Juez")
    private String xdescJuez;

    @Schema(description = "Especialista")
    private String xdescEspecialista;

    @Schema(description = "Demandante")
    private String xdescDemandante;

    @Schema(description = "Demandado")
    private String xdescDemandado;

    @Schema(description = "Ejecuciones exitosas (status 1)")
    private Long totalExitosas;

    @Schema(description = "Ejecuciones con error de archivo/FTP (status 2)")
    private Long totalErrorArchivo;

    @Schema(description = "Ejecuciones con error de Gemini (status 3)")
    private Long totalErrorIA;

    @Schema(description = "Ejecuciones iniciadas sin finalizar (status 0)")
    private Long totalEnProceso;

    @Schema(description = "Tiempo promedio de procesamiento en segundos")
    private Double tiempoPromedioSegundos;

    @Schema(description = "Tiempo mínimo de procesamiento en segundos")
    private Double tiempoMinimoSegundos;

    @Schema(description = "Tiempo máximo de procesamiento en segundos")
    private Double tiempoMaximoSegundos;

    @Schema(description = "Tiempo total de procesamiento en segundos")
    private Double tiempoTotalSegundos;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de la primera ejecución del periodo")
    private LocalDateTime fechaPrimeraOperacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de la última ejecución del periodo")
    private LocalDateTime fechaUltimaOperacion;

    @Schema(description = "ID del último registro (versión más reciente)")
    private Long idUltimoRegistro;

    @Schema(description = "Status de la última versión (0 iniciada, 1 exitosa, 2 error archivo, 3 error Gemini)")
    private Integer statusUltimoRegistro;

    @Schema(description = "Modelos de IA utilizados, separados por coma", example = "gemini-3.1-pro-preview")
    private String modelosUtilizados;
}
