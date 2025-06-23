package pj.gob.pe.metricas.model.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Entidad que representa la tabla Completions")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class Completions {

    private Long id;

    @Schema(description = "ID del usuario", example = "12345")
    private Long userId;

    @Schema(description = "Modelo utilizado", example = "GPT-4")
    private String model;

    @Schema(description = "Rol del sistema", example = "Assistant")
    private String roleSystem;

    @Schema(description = "Rol del usuario", example = "User")
    private String roleUser;

    private BigDecimal temperature;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de envío", example = "2023-10-01T12:00:00")
    private LocalDateTime fechaSend;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de respuesta", example = "2023-10-01T12:05:00")
    private LocalDateTime fechaResponse;

    @Schema(description = "ID de GPT", example = "chatcmpl-12345")
    private String idGpt;

    @Schema(description = "Objeto de la respuesta", example = "chat.completion")
    private String object;

    @Schema(description = "Timestamp de creación", example = "1696166400")
    private Long created;

    @Schema(description = "Modelo de respuesta", example = "GPT-4")
    private String modelResponse;

    @Schema(description = "Rol de la respuesta", example = "Assistant")
    private String roleResponse;

    @Schema(description = "Contenido del rol", example = "Hello, how can I help you?")
    private String roleContent;

    @Schema(description = "Razón de rechazo", example = "Content policy violation")
    private String refusal;

    @Schema(description = "Log probabilities", example = "12345")
    private Integer logprobs;

    @Schema(description = "Razón de finalización", example = "stop")
    private String finishReason;

    @Schema(description = "Tokens del prompt", example = "100")
    private Integer promptTokens;

    @Schema(description = "Tokens de la finalización", example = "200")
    private Integer completionTokens;

    @Schema(description = "Total de tokens", example = "300")
    private Integer totalTokens;

    @Schema(description = "Tokens cacheados", example = "50")
    private Integer cachedTokens;

    @Schema(description = "Tokens de audio", example = "10")
    private Integer audioTokens;

    @Schema(description = "Tokens de razonamiento", example = "20")
    private Integer completionReasoningTokens;

    @Schema(description = "Tokens de audio de la finalización", example = "5")
    private Integer completionAudioTokens;

    @Schema(description = "Tokens aceptados", example = "150")
    private Integer completionAceptedTokens;

    @Schema(description = "Tokens rechazados", example = "50")
    private Integer completionRejectedTokens;

    @Schema(description = "Nivel de servicio", example = "Premium")
    private String serviceTier;

    @Schema(description = "Huella del sistema", example = "abc123")
    private String systemFingerprint;

    @Schema(description = "Configuración asociada")
    private Integer configurationsId;

    @Schema(description = "ID único de la sesión", example = "session-12345")
    private String sessionUID;

    @Schema(description = "status de la transaccion", example = "0")
    private Integer status;

    private List<Sedes> sedes;
}
