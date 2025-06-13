package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Entidad que representa la tabla CabDocumentoGenerado")
@Entity
@Table(name = "CabDocumentoGenerado")
@Data // Lombok: Genera getters, setters, toString, equals, y hashCode
@NoArgsConstructor // Lombok: Constructor sin argumentos
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class CabDocumentoGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    private Long id;

    @Column(name = "userId", columnDefinition = "bigint DEFAULT NULL COMMENT 'ID de Usuario'")
    private Long userId;

    @Column(name = "typedoc", length = 20)
    private String typedoc;

    @Column(name = "codSede", length = 20)
    private String codSede;

    @Column(name = "codInstancia", length = 20)
    private String codInstancia;

    @Column(name = "codEspecialidad", length = 20)
    private String codEspecialidad;

    @Column(name = "codMateria", length = 20)
    private String codMateria;

    @Column(name = "sede", length = 200)
    private String sede;

    @Column(name = "instancia", length = 200)
    private String instancia;

    @Column(name = "especialidad", length = 200)
    private String especialidad;

    @Column(name = "materia", length = 200)
    private String materia;

    @Column(name = "codNumero", length = 20)
    private String codNumero;

    @Column(name = "codYear", length = 20)
    private String codYear;

    @Column(name = "idDocumento")
    private Long idDocumento;

    @Column(name = "idTipoDocumento")
    private Long idTipoDocumento;

    @Column(name = "tipoDocumento", length = 150)
    private String tipoDocumento;

    @Column(name = "documento", length = 200)
    private String documento;

    @Column(name = "nUnico", columnDefinition = "bigint unsigned DEFAULT NULL COMMENT 'Numero Unico del Expediente'")
    private Long nUnico;

    @Column(name = "xFormato", length = 50)
    private String xFormato;

    @Column(name = "ubicacion", length = 200)
    private String ubicacion;

    @Column(name = "juez", length = 200)
    private String juez;

    @Column(name = "estado", length = 200)
    private String estado;

    @Column(name = "dniDemandante", length = 100)
    private String dniDemandante;

    @Column(name = "demandante", length = 250)
    private String demandante;

    @Column(name = "dniDemandado", length = 100)
    private String dniDemandado;

    @Column(name = "demandado", length = 250)
    private String demandado;

    @Column(name = "templateCode", length = 250)
    private String templateCode;

    @Column(name = "templateID", length = 250)
    private Long templateID;

    @Column(name = "templateNombreOut", length = 250)
    private String templateNombreOut;

    @Column(name = "model", length = 50, nullable = false)
    @Schema(description = "Modelo utilizado", example = "GPT-4")
    private String model;

    @Column(name = "roleSystem", columnDefinition = "TEXT")
    @Schema(description = "Rol del sistema", example = "Assistant")
    private String roleSystem;

    @Column(name = "temperature", precision = 2, scale = 1)
    private BigDecimal temperature;

    @Column(name = "object", length = 100)
    @Schema(description = "Objeto de la respuesta", example = "chat.completion")
    private String object;

    @Column(name = "modelResponse", length = 100)
    @Schema(description = "Modelo de respuesta", example = "GPT-4")
    private String modelResponse;

    @Column(name = "roleResponse", length = 100)
    @Schema(description = "Rol de la respuesta", example = "Assistant")
    private String roleResponse;

    @Column(name = "logprobs")
    @Schema(description = "Log probabilities", example = "12345")
    private Integer logprobs;

    @Column(name = "finishReason", columnDefinition = "TEXT")
    @Schema(description = "Razón de finalización", example = "stop")
    private String finishReason;

    @Column(name = "promptTokens")
    @Schema(description = "Tokens del prompt", example = "100")
    private Integer promptTokens;

    @Column(name = "completionTokens")
    @Schema(description = "Tokens de la finalización", example = "200")
    private Integer completionTokens;

    @Column(name = "totalTokens")
    @Schema(description = "Total de tokens", example = "300")
    private Integer totalTokens;

    @Column(name = "cachedTokens")
    @Schema(description = "Tokens cacheados", example = "50")
    private Integer cachedTokens;

    @Column(name = "audioTokens")
    @Schema(description = "Tokens de audio", example = "10")
    private Integer audioTokens;

    @Column(name = "CompletionReasoningTokens")
    @Schema(description = "Tokens de razonamiento", example = "20")
    private Integer completionReasoningTokens;

    @Column(name = "CompletionAudioTokens")
    @Schema(description = "Tokens de audio de la finalización", example = "5")
    private Integer completionAudioTokens;

    @Column(name = "CompletionAceptedTokens")
    @Schema(description = "Tokens aceptados", example = "150")
    private Integer completionAceptedTokens;

    @Column(name = "CompletionRejectedTokens")
    @Schema(description = "Tokens rechazados", example = "50")
    private Integer completionRejectedTokens;

    @Column(name = "serviceTier", length = 250)
    @Schema(description = "Nivel de servicio", example = "Premium")
    private String serviceTier;

    @Column(name = "ConfigurationsId")
    @Schema(description = "Configuration Id", example = "1")
    private Integer configurationsId;

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
}
