package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Entidad que representa la tabla CabConsultaIA")
@Entity
@Table(name = "CabConsultaIA")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class CabConsultaIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la tabla", example = "1")
    private Long id;

    @Column(name = "userId")
    @Schema(description = "ID del usuario", example = "12345")
    private Long userId;

    @Column(name = "model", length = 50, nullable = false)
    @Schema(description = "Modelo utilizado", example = "GPT-4")
    private String model;

    @Column(name = "countMessages")
    @Schema(description = "Cantidad de mensajes procesados", example = "12345")
    private Integer countMessages;

    @Column(name = "firstSendMessage", columnDefinition = "TEXT")
    @Schema(description = "Primer mensaje enviado por el usuario", example = "Hola, ¿cómo estás?")
    private String firstSendMessage;

    @Column(name = "lastSendMessage", columnDefinition = "TEXT")
    @Schema(description = "Ultimo mensaje enviado por el usuario", example = "Adiós, gracias por tu ayuda.")
    private String lastSendMessage;

    @Column(name = "firstResponseMessage", columnDefinition = "TEXT")
    @Schema(description = "Primer mensaje de respuesta de la IA", example = "Hola, estoy aquí para ayudarte.")
    private String firstResponseMessage;

    @Column(name = "lastResponseMessage", columnDefinition = "TEXT")
    @Schema(description = "Ultimo mensaje de respuesta de la IA", example = "Gracias por tu consulta, ¡hasta luego!")
    private String lastResponseMessage;

    @Column(name = "sessionUID", length = 50, nullable = false)
    @Schema(description = "ID único de la sesión", example = "session-12345")
    private String sessionUID;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="regDate", nullable = true)
    private LocalDate regDate;

    @Schema(description = "Fecha y Hora de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="regDatetime", nullable = true)
    private LocalDateTime regDatetime;

    @Schema(description = "Epoch de Creación del Registro")
    @Column(name="regTimestamp", nullable = true)
    private Long regTimestamp;

    @Schema(description = "Fecha de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="updDate", nullable = true)
    private LocalDate updDate;

    @Schema(description = "Fecha y Hora de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updDatetime", nullable = true)
    private LocalDateTime updDatetime;

    @Schema(description = "Epoch de Edición del Registro")
    @Column(name="updTimestamp", nullable = true)
    private Long updTimestamp;
}
