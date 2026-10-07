package pj.gob.pe.metricas.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Espejo de pj.gob.pe.judicial.model.beans.VariableDocumentoToKafka (tópico judicial-documentos-generado-v2). */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariableDocumentoToKafka {

    private String nombre;
    private String tipo;
    private String campoSistema;
    private Boolean tieneValor;
}
