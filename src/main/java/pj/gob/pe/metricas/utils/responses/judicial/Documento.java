package pj.gob.pe.metricas.utils.responses.judicial;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Documento Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documento {

    private Long idDocumento;
    private Long idTipoDocumento;
    private String descripcion;
    private String codigoTemplate;
}
