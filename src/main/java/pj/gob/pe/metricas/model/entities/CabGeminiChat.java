package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Cabecera por conversación del chat con Gemini (métricas). Espejo de {@link CabConsultaIA}
 * (chat ChatGPT) con los indicadores de adjuntos del módulo Gemini.
 */
@Schema(description = "Entidad que representa la tabla CabGeminiChat")
@Entity
@Table(name = "CabGeminiChat")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class CabGeminiChat {

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

    @Column(name = "countMessages")
    @Schema(description = "Cantidad de mensajes de la conversación", example = "5")
    private Integer countMessages;

    @Column(name = "firstSendMessage", columnDefinition = "TEXT")
    @Schema(description = "Primer prompt enviado por el usuario")
    private String firstSendMessage;

    @Column(name = "lastSendMessage", columnDefinition = "TEXT")
    @Schema(description = "Último prompt enviado por el usuario")
    private String lastSendMessage;

    @Column(name = "firstResponseMessage", columnDefinition = "TEXT")
    @Schema(description = "Primera respuesta de la IA")
    private String firstResponseMessage;

    @Column(name = "lastResponseMessage", columnDefinition = "TEXT")
    @Schema(description = "Última respuesta de la IA")
    private String lastResponseMessage;

    @Column(name = "sessionUID", length = 50, nullable = false)
    @Schema(description = "UUID de la conversación", example = "session-12345")
    private String sessionUID;

    @Column(name = "hasFiles")
    @Schema(description = "1 si algún turno de la conversación incluyó adjuntos", example = "1")
    private Integer hasFiles;

    @Column(name = "countFiles")
    @Schema(description = "Cantidad total de adjuntos de la conversación", example = "3")
    private Integer countFiles;

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

    @Schema(description = "Fecha de Edición del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updDate", nullable = true)
    private LocalDate updDate;

    @Schema(description = "Fecha y Hora de Edición del Registro")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updDatetime", nullable = true)
    private LocalDateTime updDatetime;

    @Schema(description = "Epoch de Edición del Registro")
    @Column(name = "updTimestamp", nullable = true)
    private Long updTimestamp;
}
