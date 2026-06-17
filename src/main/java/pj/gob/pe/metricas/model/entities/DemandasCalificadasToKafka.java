package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que persiste las demandas calificadas recibidas por Kafka (tópico
 * judicial-metrics-califications) desde ms-jurisia-consultaia. Mapea la tabla
 * JURISDB_METRICS.DemandasCalificadas. Incluye los campos de la calificación más los datos del
 * usuario que la generó.
 */
@Schema(description = "Entidad que representa la tabla DemandasCalificadas (métricas)")
@Entity
@Table(name = "DemandasCalificadas")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandasCalificadasToKafka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    @Schema(description = "ID único de la demanda calificada", example = "1")
    private Long id;

    @Column(name = "nUnico")
    private Long nUnico;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "roleSystem", columnDefinition = "TEXT")
    private String roleSystem;

    @Column(name = "temperature", precision = 3, scale = 1)
    private BigDecimal temperature;

    @Column(name = "fechaSend")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaSend;

    @Column(name = "fechaResponse")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaResponse;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Column(name = "timeSeconds")
    private Double timeSeconds;

    @Column(name = "ConfigurationsId", nullable = false)
    private Integer configurationsId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "anio", length = 10)
    private String anio;

    @Column(name = "expNro", length = 20)
    private String expNro;

    @Column(name = "tipoExpediente", length = 50)
    private String tipoExpediente;

    @Column(name = "rutaCompleta", length = 100)
    private String rutaCompleta;

    @Column(name = "xformato", length = 50)
    private String xformato;

    @Column(name = "cclave", length = 50)
    private String cclave;

    @Column(name = "xnomInstancia", length = 100)
    private String xnomInstancia;

    @Column(name = "cubicacion", length = 20)
    private String cubicacion;

    @Column(name = "cinstancia", length = 20)
    private String cinstancia;

    @Column(name = "xdescEstado", length = 50)
    private String xdescEstado;

    @Column(name = "cusuario", length = 50)
    private String cusuario;

    @Column(name = "cmateria", length = 20)
    private String cmateria;

    @Column(name = "cespecialidad", length = 20)
    private String cespecialidad;

    @Column(name = "xdescUbicacion", length = 100)
    private String xdescUbicacion;

    @Column(name = "xnombreArchivo", length = 500)
    private String xnombreArchivo;

    @Column(name = "nincidente", length = 20)
    private String nincidente;

    @Column(name = "xrutaArchivo", length = 100)
    private String xrutaArchivo;

    @Column(name = "xdescMateria", length = 100)
    private String xdescMateria;

    @Column(name = "finicio")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finicio;

    @Column(name = "xdescJuez", length = 200)
    @Schema(description = "Descripción del nombre del Juez", example = "Juan Perez")
    private String xdescJuez;

    @Column(name = "xdescEspecialista", length = 200)
    @Schema(description = "Descripción del nombre del Especialista", example = "Rosa Suarez")
    private String xdescEspecialista;

    @Column(name = "xdescDemandado", length = 200)
    @Schema(description = "Descripción del nombre del Demandado", example = "Mario Lopez")
    private String xdescDemandado;

    @Column(name = "xdescDemandante", length = 200)
    @Schema(description = "Descripción del nombre del Demandante", example = "Maria Ruiz")
    private String xdescDemandante;

    // ===== Datos del usuario que generó la calificación (sesión) =====
    @Column(name = "idUser")
    private Long idUser;

    @Column(name = "tipoDocumento")
    private Integer tipoDocumento;

    @Column(name = "documento", length = 20)
    private String documento;

    @Column(name = "apellidos", length = 200)
    private String apellidos;

    @Column(name = "nombres", length = 200)
    private String nombres;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "genero")
    private Integer genero;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "activo")
    private Integer activo;

    @Column(name = "idDependencia")
    private Long idDependencia;

    @Column(name = "nombreDependencia", length = 250)
    private String nombreDependencia;

    @Column(name = "codigoDependencia", length = 50)
    private String codigoDependencia;

    @Column(name = "siglaDependencia", length = 50)
    private String siglaDependencia;

    @Column(name = "cargo", length = 200)
    private String cargo;

    @Column(name = "idTipoUser")
    private Long idTipoUser;

    @Column(name = "tipoUser", length = 100)
    private String tipoUser;
}
