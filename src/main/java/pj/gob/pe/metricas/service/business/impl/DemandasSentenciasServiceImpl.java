package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.dao.DemandasSentenciasToKafkaDAO;
import pj.gob.pe.metricas.exception.ValidationSessionServiceException;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.service.business.DemandasSentenciasService;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.Constantes;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.demandassentencias.InputDemandasSentencias;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DemandasSentenciasServiceImpl implements DemandasSentenciasService {

    private final SecurityService securityService;
    private final DemandasSentenciasToKafkaDAO demandasSentenciasToKafkaDAO;

    @Override
    public void RegistrarDemandaSentencia(DemandasSentenciasToKafka demandasSentenciasToKafka) throws Exception {
        // El mensaje llega con el id de consultaia; se descarta para que la BD de métricas
        // genere su propio id (AUTO_INCREMENT) y la operación sea un INSERT (no un merge/update).
        demandasSentenciasToKafka.setId(null);
        this.demandasSentenciasToKafkaDAO.registrar(demandasSentenciasToKafka);
    }

    @Override
    public Page<DemandasSentenciasToKafka> getDemandasSentencias(
            String SessionId,
            InputDemandasSentencias inputData,
            Pageable pageable) {

        String errorValidacion = "";

        if(SessionId == null || SessionId.isEmpty()){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        ResponseLogin responseLogin = securityService.GetSessionData(SessionId);

        if(responseLogin == null || !responseLogin.isSuccess() || !responseLogin.isItemFound() || responseLogin.getUser() == null){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        Map<String, Object> filters = new HashMap<>();
        if(Objects.equals(responseLogin.getUser().getIdTipoUser(), Constantes.USER_NORMAL_2)) {
            filters.put("userId", responseLogin.getUser().getIdUser());
        } else {
            if(inputData.getIdUser() != null && inputData.getIdUser() > 0) {
                filters.put("userId", inputData.getIdUser());
            } else{
                //Consultar por usuarios
                boolean consultaPorUsuarios = false;

                InputConsultaIAExternal inputConsultaIAExternal = new InputConsultaIAExternal();

                if(inputData.getDocumento() != null && !inputData.getDocumento().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setDocumento(inputData.getDocumento());
                }
                if(inputData.getNombres() != null && !inputData.getNombres().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setNombres(inputData.getNombres());
                }
                if(inputData.getApellidos() != null && !inputData.getApellidos().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setApellidos(inputData.getApellidos());
                }
                if(inputData.getCargo() != null && !inputData.getCargo().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setCargo(inputData.getCargo());
                }
                if(inputData.getUsername() != null && !inputData.getUsername().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setUsername(inputData.getUsername());
                }
                if(inputData.getEmail() != null && !inputData.getEmail().isEmpty()) {
                    consultaPorUsuarios = true;
                    inputConsultaIAExternal.setEmail(inputData.getEmail());
                }

                if(consultaPorUsuarios){
                    List<OutputConsultaIAExternal> listUsers = securityService.Getusers(inputConsultaIAExternal);

                    if(listUsers == null || listUsers.isEmpty()) {
                        return new PageImpl<>(Collections.emptyList(), pageable, 0);
                    }

                    // Agregar los IDs de los usuarios encontrados
                    filters.put("list_userId", listUsers.stream().map(OutputConsultaIAExternal::getId).toList());
                }
            }
        }

        if(inputData.getNUnico() != null && inputData.getNUnico() > 0) {
            filters.put("nUnico", inputData.getNUnico());
        }
        if(inputData.getModel() != null && !inputData.getModel().isEmpty()) {
            filters.put("model", inputData.getModel());
        }
        if(inputData.getStatus() != null) {
            filters.put("status", inputData.getStatus());
        }
        if(inputData.getAnio() != null && !inputData.getAnio().isEmpty()) {
            filters.put("anio", inputData.getAnio());
        }
        if(inputData.getExpNro() != null && !inputData.getExpNro().isEmpty()) {
            filters.put("expNro", inputData.getExpNro());
        }
        if(inputData.getTipoExpediente() != null && !inputData.getTipoExpediente().isEmpty()) {
            filters.put("tipoExpediente", inputData.getTipoExpediente());
        }
        if(inputData.getCinstancia() != null && !inputData.getCinstancia().isEmpty()) {
            filters.put("cinstancia", inputData.getCinstancia());
        }
        if(inputData.getCespecialidad() != null && !inputData.getCespecialidad().isEmpty()) {
            filters.put("cespecialidad", inputData.getCespecialidad());
        }
        if(inputData.getCmateria() != null && !inputData.getCmateria().isEmpty()) {
            filters.put("cmateria", inputData.getCmateria());
        }
        if(inputData.getCubicacion() != null && !inputData.getCubicacion().isEmpty()) {
            filters.put("cubicacion", inputData.getCubicacion());
        }
        if(inputData.getXdescEstado() != null && !inputData.getXdescEstado().isEmpty()) {
            filters.put("xdescEstado", inputData.getXdescEstado());
        }

        Map<String, Object> filtersFecha = new HashMap<>();
        Map<String, Object> filtersNotEquals = new HashMap<>();
        if(inputData.getFechaInicio() != null && inputData.getFechaFin() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        } else if(inputData.getFechaInicio() != null) {
            filtersFecha.put("fechaInicio", inputData.getFechaInicio());
        } else if(inputData.getFechaFin() != null) {
            filtersFecha.put("fechaFin", inputData.getFechaFin());
        }

        return demandasSentenciasToKafkaDAO.getGeneralDemandasSentencias(filters, filtersNotEquals, filtersFecha, pageable);
    }

    @Override
    public Long getTotalDemandasSentencias(String buscar, String SessionId) throws Exception {

        String errorValidacion = "";

        if(SessionId == null || SessionId.isEmpty()){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        ResponseLogin responseLogin = securityService.GetSessionData(SessionId);

        if(responseLogin == null || !responseLogin.isSuccess() || !responseLogin.isItemFound() || responseLogin.getUser() == null){
            errorValidacion = "La sessión remitida es inválida";
            throw new ValidationSessionServiceException(errorValidacion);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("userId", responseLogin.getUser().getIdUser());

        Map<String, Object> filtersNotEquals = new HashMap<>();

        return demandasSentenciasToKafkaDAO.getTotalDemandasSentencias(filters, filtersNotEquals);
    }
}
