package pj.gob.pe.metricas.model.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Adjunto de un turno del chat con Gemini, tal como llega dentro del payload del tópico
 * judicial-metrics-gemini-chats (lista {@code files} de {@link GeminiChatToKafka}). Corresponde a
 * la entidad GeminiChatsFiles de ms-jurisia-consultaia.
 */
@Schema(description = "Adjunto de un turno del chat Gemini (payload Kafka)")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiChatFileToKafka {

    private Long id;
    private Long geminiChatId;
    private String sessionUID;
    private String fileName;
    private String mimeType;
    private Long sizeBytes;
    private String gcsUri;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaReg;

    private Integer status;
}
