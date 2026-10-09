package pj.gob.pe.metricas.utils.responses.demandasadmin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Fila del reporte admin de calificaciones/sentencias agrupadas por sede e instancia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminDemandasPorInstancia {

    @Schema(description = "Código de sede SIJ", example = "0201")
    private String codSede;

    @Schema(description = "Descripción de la sede SIJ", example = "Sede Central de Corte")
    private String sede;

    @Schema(description = "Código de instancia SIJ", example = "301")
    private String cinstancia;

    @Schema(description = "Nombre de la instancia", example = "1° JUZGADO FAMILIA - Sede San Martin")
    private String xnomInstancia;

    @Schema(description = "Usuarios distintos que realizaron la operación en la instancia")
    private Long totalUsuarios;

    @Schema(description = "Expedientes distintos (nUnico) con la operación en la instancia")
    private Long totalExpedientes;

    @Schema(description = "Total de calificaciones o sentencias ejecutadas (todas las versiones)")
    private Long totalRegistros;

    @Schema(description = "Ejecuciones exitosas (status 1)")
    private Long totalExitosas;

    @Schema(description = "Ejecuciones con error de archivo/FTP (status 2)")
    private Long totalErrorArchivo;

    @Schema(description = "Ejecuciones con error de Gemini (status 3)")
    private Long totalErrorIA;

    @Schema(description = "Ejecuciones iniciadas sin finalizar (status 0)")
    private Long totalEnProceso;

    @Schema(description = "Porcentaje de ejecuciones exitosas sobre el total", example = "85.5")
    private Double porcentajeExito;

    @Schema(description = "Promedio de ejecuciones por expediente", example = "2.5")
    private Double promedioPorExpediente;

    @Schema(description = "Materias distintas trabajadas")
    private Long totalMaterias;

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

    @Schema(description = "Modelos de IA utilizados, separados por coma", example = "gemini-3.1-pro-preview")
    private String modelosUtilizados;
}
