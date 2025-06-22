package pj.gob.pe.metricas.utils.responses.docgenerados;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response Total Filters Documentos Generados Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTotalFiltersDocGenerados {


    private String typedoc;
    private String codSede;
    private String codInstancia;
    private String codEspecialidad;
    private String codMateria;
    private String sede;
    private String instancia;
    private String especialidad;
    private String materia;
    private Long idDocumento;
    private Long idTipoDocumento;
    private String tipoDocumento;
    private String documento;
    private String ubicacion;
    private String juez;
    private String estado;
    private String dniDemandante;
    private String demandante;
    private String dniDemandado;
    private String demandado;
    private String templateCode;
    private Long templateID;
    private String templateNombreOut;
    private String model;
    private String numeroExpediente;
    private String yearExpediente;
    private String xdeFormato;
    private Long numUnico;

    private Long totalDocsGenerados;

}
