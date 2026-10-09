package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pj.gob.pe.metricas.exception.AccesoDenegadoException;
import pj.gob.pe.metricas.exception.ValidationServiceException;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.Constantes;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Validaciones comunes de los reportes de administrador (calificaciones, sentencias y documentos
 * generados por plantilla).
 */
@Component
@RequiredArgsConstructor
public class ValidacionAdministrador {

    private final SecurityService securityService;

    /**
     * Los reportes admin solo están disponibles para Super Administrador, Administrador y Reportes.
     * Sesión inválida: 401; sesión válida sin permisos: 403.
     */
    public void validarSesionAdministrador(String SessionId) {

        if(SessionId == null || SessionId.isEmpty()){
            throw new ValidationSessionServiceException("La sessión remitida es inválida");
        }

        ResponseLogin responseLogin = securityService.GetSessionData(SessionId);

        if(responseLogin == null || !responseLogin.isSuccess() || !responseLogin.isItemFound() || responseLogin.getUser() == null){
            throw new ValidationSessionServiceException("La sessión remitida es inválida");
        }

        Long idTipoUser = responseLogin.getUser().getIdTipoUser();

        if(!Objects.equals(idTipoUser, Constantes.USER_SUPER_ADMINISTRADOR)
                && !Objects.equals(idTipoUser, Constantes.USER_ADMINISTRADOR)
                && !Objects.equals(idTipoUser, Constantes.USER_NORMAL)
                && !Objects.equals(idTipoUser, Constantes.USER_REPORTES)) {
            throw new AccesoDenegadoException("El usuario no tiene permisos para acceder a los reportes de administrador");
        }
    }

    public void validarRangoFechas(LocalDate fechaInicial, LocalDate fechaFinal) {
        if(fechaInicial.isAfter(fechaFinal)) {
            throw new ValidationServiceException("La fecha inicial no puede ser mayor a la fecha final");
        }
    }
}
