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
 * Enlace del detalle (turno) del chat Gemini con el SIJ (sedes/instancias del usuario).
 * Espejo de {@link SIJDetailConsultaIA} para el módulo Gemini.
 */
@Schema(description = "Entidad que representa la tabla SIJDetailGeminiChat")
@Entity
@Table(name = "SIJDetailGeminiChat")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class SIJDetailGeminiChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la tabla", example = "1")
    private Long id;

    @Column(name = "userId")
    @Schema(description = "ID del usuario", example = "12345")
    private Long userId;

    @Column(name = "detailGeminiChatId")
    @Schema(description = "ID del detalle (DetailGeminiChat) del turno", example = "10")
    private Long detailGeminiChatId;

    @Column(name = "codSede", length = 20)
    private String codSede;

    @Column(name = "codInstancia", length = 20)
    private String codInstancia;

    @Column(name = "sede", length = 200)
    private String sede;

    @Column(name = "instancia", length = 200)
    private String instancia;

    @Column(name = "sessionUID", length = 50)
    @Schema(description = "UUID de la conversación con Gemini", example = "session-12345")
    private String sessionUID;

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
