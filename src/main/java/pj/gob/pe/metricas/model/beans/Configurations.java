package pj.gob.pe.metricas.model.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Configurations Model")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class Configurations {

    @Schema(description = "ID único de la configuración", example = "1")
    private Integer id;

    @Schema(description = "Código del servicio", example = "SVC001")
    private String serviceCode;

    @Schema(description = "Model de GPT", example = "gpt-4o")
    private String model;

    @Schema(description = "Descripción de la configuración", example = "Configuración inicial")
    private String descripcion;

    @Schema(description = "Rol del sistema", example = "Admin")
    private String roleSystem;

    @Schema(description = "Prompt por defecto", example = "Bienvenido")
    private String promptDefault;

    @Schema(description = "Número máximo de mensajes permitidos", example = "100")
    private Integer maxMessages;

    private BigDecimal temperature;

    @Schema(description = "Estado del Registro")
    private Integer activo;

    @Schema(description = "Borrado Lógico del Registro")
    private Integer borrado;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate regDate;

    @Schema(description = "Fecha y Hora de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDatetime;

    @Schema(description = "Epoch de Creación del Registro")
    private Long regTimestamp;

    @Schema(description = "Usuario que insertó el registro")
    private Long regUserId;

    @Schema(description = "Fecha de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate updDate;

    @Schema(description = "Fecha y Hora de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updDatetime;

    @Schema(description = "Epoch de Edición del Registro")
    private Long updTimestamp;

    @Schema(description = "Usuario que editó el registro")
    private Long updUserId;
}
