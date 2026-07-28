package pj.gob.pe.metricas.model.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Payload del tópico judicial-metrics-gemini-chats, publicado por ms-jurisia-consultaia
 * (clase origen: pj.gob.pe.consultaia.utils.beans.responses.ResponseGeminiChat). Un turno del
 * chat conversacional con Gemini, con sus adjuntos y las sedes/instancias del usuario.
 */
@Schema(description = "Turno del chat Gemini recibido por Kafka para métricas")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiChatToKafka {

    private Long id;
    private Long userId;
    private String model;
    private String roleSystem;
    private String prompt;
    private BigDecimal temperature;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaSend;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaResponse;

    private String response;
    private Double timeSeconds;
    private String sessionUID;
    private Integer status;
    private Integer hasFiles;
    private Integer configurationsId;

    private List<GeminiChatFileToKafka> files;

    private List<Sedes> sedes;
}
