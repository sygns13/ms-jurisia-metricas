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
 * Cabecera de cada documento generado por plantilla completa (ms-jurisia-judicial,
 * /v1/documento-plantilla), exitoso o fallido. Una fila por sessionUID.
 */
@Schema(description = "Entidad que representa la tabla CabDocumentoPlantillaGenerado")
@Entity
@Table(name = "CabDocumentoPlantillaGenerado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CabDocumentoPlantillaGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    private Long id;

    // ---- Generación ----
    @Column(name = "sessionUID", length = 50, nullable = false)
    private String sessionUID;

    @Column(name = "typedoc", length = 20)
    private String typedoc;

    @Schema(description = "1 exitoso, 2 error")
    @Column(name = "status")
    private Integer status;

    @Column(name = "mensajeError", length = 1000)
    private String mensajeError;

    // ---- Usuario ----
    @Column(name = "userId")
    private Long userId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "nombreUsuario", length = 250)
    private String nombreUsuario;

    @Column(name = "cargo", length = 200)
    private String cargo;

    @Column(name = "idDependencia")
    private Long idDependencia;

    @Column(name = "dependencia", length = 250)
    private String dependencia;

    // ---- Expediente (SIJ) ----
    @Column(name = "nUnico")
    private Long nUnico;

    @Column(name = "numIncidente", length = 20)
    private String numIncidente;

    @Column(name = "codSede", length = 20)
    private String codSede;

    @Column(name = "sede", length = 200)
    private String sede;

    @Column(name = "codInstancia", length = 20)
    private String codInstancia;

    @Column(name = "instancia", length = 200)
    private String instancia;

    @Column(name = "codEspecialidad", length = 20)
    private String codEspecialidad;

    @Column(name = "especialidad", length = 200)
    private String especialidad;

    @Column(name = "codMateria", length = 20)
    private String codMateria;

    @Column(name = "materia", length = 200)
    private String materia;

    @Column(name = "codNumero", length = 20)
    private String codNumero;

    @Column(name = "codYear", length = 20)
    private String codYear;

    @Column(name = "xFormato", length = 100)
    private String xFormato;

    @Column(name = "ubicacion", length = 200)
    private String ubicacion;

    @Column(name = "juez", length = 200)
    private String juez;

    @Column(name = "especialista", length = 200)
    private String especialista;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "dniDemandante", length = 500)
    private String dniDemandante;

    @Column(name = "demandante", columnDefinition = "TEXT")
    private String demandante;

    @Column(name = "dniDemandado", length = 500)
    private String dniDemandado;

    @Column(name = "demandado", columnDefinition = "TEXT")
    private String demandado;

    @Column(name = "cantidadDemandantes")
    private Integer cantidadDemandantes;

    @Column(name = "cantidadDemandados")
    private Integer cantidadDemandados;

    // ---- Documento / plantilla ----
    @Column(name = "idTipoDocumento")
    private Long idTipoDocumento;

    @Column(name = "tipoDocumento", length = 150)
    private String tipoDocumento;

    @Column(name = "idDocumento")
    private Long idDocumento;

    @Column(name = "documento", length = 200)
    private String documento;

    @Column(name = "idPlantilla")
    private Long idPlantilla;

    @Column(name = "codigoPlantilla", length = 50)
    private String codigoPlantilla;

    @Column(name = "nombreOutPlantilla", length = 150)
    private String nombreOutPlantilla;

    @Column(name = "versionPlantilla")
    private Integer versionPlantilla;

    @Column(name = "corregirIA")
    private Integer corregirIA;

    @Column(name = "tamanioPlantillaBytes")
    private Long tamanioPlantillaBytes;

    @Column(name = "tamanioSalidaBytes")
    private Long tamanioSalidaBytes;

    // ---- Variables ----
    @Column(name = "totalVariables")
    private Integer totalVariables;

    @Column(name = "variablesSij")
    private Integer variablesSij;

    @Column(name = "variablesCalculadas")
    private Integer variablesCalculadas;

    @Column(name = "variablesManuales")
    private Integer variablesManuales;

    @Column(name = "variablesIA")
    private Integer variablesIA;

    @Column(name = "variablesNoDefinidas")
    private Integer variablesNoDefinidas;

    @Column(name = "variablesSinValor")
    private Integer variablesSinValor;

    @Column(name = "reemplazosRealizados")
    private Integer reemplazosRealizados;

    // ---- IA ----
    @Schema(description = "NO_APLICA, EXITOSO o ERROR")
    @Column(name = "estadoIA", length = 20)
    private String estadoIA;

    @Column(name = "mensajeIA", length = 1000)
    private String mensajeIA;

    @Column(name = "parrafosEnviadosIA")
    private Integer parrafosEnviadosIA;

    @Column(name = "parrafosCorregidosIA")
    private Integer parrafosCorregidosIA;

    @Column(name = "bloquesSolicitadosIA")
    private Integer bloquesSolicitadosIA;

    @Column(name = "bloquesGeneradosIA")
    private Integer bloquesGeneradosIA;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "roleSystem", columnDefinition = "TEXT")
    private String roleSystem;

    @Column(name = "temperature", precision = 3, scale = 1)
    private BigDecimal temperature;

    @Column(name = "configurationsId")
    private Integer configurationsId;

    @Column(name = "finishReason", length = 50)
    private String finishReason;

    @Column(name = "promptTokens")
    private Integer promptTokens;

    @Column(name = "candidatesTokens")
    private Integer candidatesTokens;

    @Column(name = "thoughtsTokens")
    private Integer thoughtsTokens;

    @Column(name = "cachedTokens")
    private Integer cachedTokens;

    @Column(name = "totalTokens")
    private Integer totalTokens;

    // ---- Tiempos ----
    @Column(name = "tiempoSijMs")
    private Long tiempoSijMs;

    @Column(name = "tiempoIAMs")
    private Long tiempoIAMs;

    @Column(name = "tiempoTotalMs")
    private Long tiempoTotalMs;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "regDate")
    private LocalDate regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "regDatetime")
    private LocalDateTime regDatetime;

    @Column(name = "regTimestamp")
    private Long regTimestamp;
}
