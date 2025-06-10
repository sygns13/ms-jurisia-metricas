package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

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
