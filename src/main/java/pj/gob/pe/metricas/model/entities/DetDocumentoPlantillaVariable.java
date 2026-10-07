package pj.gob.pe.metricas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Detalle por variable de cada documento generado por plantilla (cómo se resolvió, sin el valor). */
@Schema(description = "Entidad que representa la tabla DetDocumentoPlantillaVariable")
@Entity
@Table(name = "DetDocumentoPlantillaVariable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetDocumentoPlantillaVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned")
    private Long id;

    @Schema(description = "ID de CabDocumentoPlantillaGenerado")
    @Column(name = "idCab")
    private Long idCab;

    @Column(name = "sessionUID", length = 50)
    private String sessionUID;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "idPlantilla")
    private Long idPlantilla;

    @Column(name = "codigoPlantilla", length = 50)
    private String codigoPlantilla;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Schema(description = "SIJ, CALCULADA, MANUAL, IA o NO_DEFINIDA")
    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "campoSistema", length = 100)
    private String campoSistema;

    @Schema(description = "1 con valor, 0 quedó como '...'")
    @Column(name = "tieneValor")
    private Integer tieneValor;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "regDate")
    private LocalDate regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "regDatetime")
    private LocalDateTime regDatetime;

    @Column(name = "regTimestamp")
    private Long regTimestamp;
}
