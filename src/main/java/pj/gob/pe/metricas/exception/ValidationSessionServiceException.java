package pj.gob.pe.metricas.exception;

public class ValidationSessionServiceException  extends RuntimeException{

    private static final long serialVersionUID = -2783311501035261954L;

    public ValidationSessionServiceException(String mensaje) {
        super(mensaje);
    }
}
