package pj.gob.pe.metricas.exception;

/**
 * Sesión válida pero sin permisos para el recurso solicitado (HTTP 403).
 */
public class AccesoDenegadoException extends RuntimeException{

    private static final long serialVersionUID = 4127381102965530417L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
