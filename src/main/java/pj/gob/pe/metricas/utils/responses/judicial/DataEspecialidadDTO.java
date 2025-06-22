package pj.gob.pe.metricas.utils.responses.judicial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "Data de Especialidad SIJ Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEspecialidadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoEspecialidad;
    private String especialidad;
    private String codigoCodEspecialidad;
    private String codigoInstancia;
}
