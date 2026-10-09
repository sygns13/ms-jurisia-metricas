package pj.gob.pe.metricas.utils.responses.docgeneradosplantilla;

/**
 * Datos adicionales del flujo por plantilla que se agregan a los responses de
 * /v1/documento-generado-ia sin alterar los campos del contrato anterior.
 */
public interface MetricasPlantillaExtra {

    void setTotalErrores(Long totalErrores);

    void setIaExitoso(Long iaExitoso);

    void setIaError(Long iaError);

    void setTotalTokens(Long totalTokens);

    void setTiempoPromedioTotalMs(Double tiempoPromedioTotalMs);
}
