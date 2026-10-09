package pj.gob.pe.metricas.utils.responses.docgeneradosplantilla;

/**
 * Nodo del resumen main (instancia, especialidad, tipo de documento o documento): totales doc/web
 * del contrato anterior más los datos adicionales del flujo por plantilla.
 */
public interface ResponseMainNodo extends MetricasPlantillaExtra {

    void setTotalDoc(Long totalDoc);

    void setTotalWeb(Long totalWeb);
}
