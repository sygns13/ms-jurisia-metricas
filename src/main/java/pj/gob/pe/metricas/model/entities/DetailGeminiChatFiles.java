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
 * Adjunto de un turno del chat con Gemini (métricas): nombre original del archivo y su
 * dirección en GCS (URI gs://). Se enlaza al turno por {@code detailGeminiChatId}.
 */
@Schema(description = "Entidad que representa la tabla DetailGeminiChatFiles")
@Entity
@Table(name = "DetailGeminiChatFiles")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class DetailGeminiChatFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la tabla", example = "1")
    private Long id;

    @Column(name = "detailGeminiChatId")
    @Schema(description = "ID del detalle (DetailGeminiChat) del turno", example = "10")
    private Long detailGeminiChatId;

    @Column(name = "userId")
    @Schema(description = "ID del usuario", example = "12345")
    private Long userId;

    @Column(name = "sessionUID", length = 50, nullable = false)
    @Schema(description = "UUID de la conversación", example = "session-12345")
    private String sessionUID;

    @Column(name = "fileName", length = 255)
    @Schema(description = "Nombre original del archivo adjunto", example = "demanda_alimentos.pdf")
    private String fileName;

    @Column(name = "mimeType", length = 150)
    @Schema(description = "MIME type del archivo", example = "application/pdf")
    private String mimeType;

    @Column(name = "sizeBytes")
    @Schema(description = "Tamaño del archivo en bytes", example = "204800")
    private Long sizeBytes;

    @Column(name = "gcsUri", length = 500)
    @Schema(description = "Dirección del archivo en GCS", example = "gs://pj_gemini_chat_adjuntos/chats/uuid/archivo.pdf")
    private String gcsUri;

    @Column(name = "status")
    @Schema(description = "Status del adjunto (1 activo)", example = "1")
    private Integer status;

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
