package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Detalle por turno/mensaje del chat con Gemini (métricas). Espejo de {@link DetailConsultaIA}
 * (chat ChatGPT) con los campos propios del módulo Gemini (timeSeconds, hasFiles).
 */
@Schema(description = "Entidad que representa la tabla DetailGeminiChat")
@Entity
@Table(name = "DetailGeminiChat")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class DetailGeminiChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la tabla", example = "1")
    private Long id;

    @Column(name = "userId")
    @Schema(description = "ID del usuario", example = "12345")
    private Long userId;

    @Column(name = "model", length = 50)
    @Schema(description = "Modelo Gemini utilizado", example = "gemini-3.8-flash")
    private String model;

    @Column(name = "roleSystem", columnDefinition = "TEXT")
    @Schema(description = "Instrucción de sistema utilizada")
    private String roleSystem;

    @Column(name = "sendMessage", columnDefinition = "TEXT")
    @Schema(description = "Prompt enviado por el usuario")
    private String sendMessage;

    @Column(name = "temperature", precision = 3, scale = 1)
    private BigDecimal temperature;

    @Column(name = "fechaSend")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de envío", example = "2026-07-28T12:00:00")
    private LocalDateTime fechaSend;

    @Column(name = "fechaResponse")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de respuesta", example = "2026-07-28T12:05:00")
    private LocalDateTime fechaResponse;

    @Column(name = "responseMessage", columnDefinition = "TEXT")
    @Schema(description = "Respuesta generada por Gemini")
    private String responseMessage;

    @Column(name = "timeSeconds")
    @Schema(description = "Tiempo total de procesamiento en segundos", example = "12.34")
    private Double timeSeconds;

    @Column(name = "ConfigurationsId")
    @Schema(description = "ID de la Configuración usada en consultaia", example = "5")
    private Integer configurationsId;

    @Column(name = "sessionUID", length = 50, nullable = false)
    @Schema(description = "UUID de la conversación", example = "session-12345")
    private String sessionUID;

    @Column(name = "status")
    @Schema(description = "Status del turno (0 iniciado, 1 exitoso, 2 error)", example = "1")
    private Integer status;

    @Column(name = "hasFiles")
    @Schema(description = "1 si el turno incluyó adjuntos", example = "0")
    private Integer hasFiles;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "regDate", nullable = true)
    private LocalDate regDate;

    @Schema(description = "Fecha y Hora de Creación del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "regDatetime", nullable = true)
    private LocalDateTime regDatetime;

    @Schema(description = "Epoch de Creación del Registro")
    @Column(name = "regTimestamp", nullable = true)
    private Long regTimestamp;
}
