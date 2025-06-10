package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.dao.CabConsultaIADAO;
import pj.gob.pe.metricas.dao.DetailConsultaIADAO;
import pj.gob.pe.metricas.model.beans.Completions;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.service.business.ConsultaIAService;
import pj.gob.pe.metricas.utils.Constantes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class ConsultaIAServiceImpl implements ConsultaIAService {

    private final ConfigProperties configProperties;
    private final CabConsultaIADAO cabConsultaIADAO;
    private final DetailConsultaIADAO detailConsultaIADAO;

    @Override
    public void RegistrarConsultaIA(Completions completions) throws Exception {

        //Verificar si la conversacion ya existe
        CabConsultaIA completionsExistente = cabConsultaIADAO.findBySessionUID(completions.getSessionUID());
        LocalDateTime fechaActualTime = LocalDateTime.now();

        if(completionsExistente == null || completionsExistente.getId() == null) {

            //Registrar la cabecera de la consulta
            CabConsultaIA cabConsultaIA = new CabConsultaIA();

            cabConsultaIA.setUserId(completions.getUserId());
            cabConsultaIA.setCountMessages(Constantes.CANTIDAD_UNIDAD_INTEGER);
            cabConsultaIA.setFirstSendMessage(completions.getRoleUser());
            cabConsultaIA.setLastSendMessage(completions.getRoleUser());
            cabConsultaIA.setFirstResponseMessage(completions.getRoleContent());
            cabConsultaIA.setLastResponseMessage(completions.getRoleContent());
            cabConsultaIA.setSessionUID(completions.getSessionUID());
            cabConsultaIA.setRegDate(fechaActualTime.toLocalDate());
            cabConsultaIA.setRegDatetime(fechaActualTime);
            cabConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));


            //Guardar la cabecera
            cabConsultaIADAO.registrar(cabConsultaIA);
        } else {
            //Actualizar la cabecera de la consulta
            completionsExistente.setCountMessages(completionsExistente.getCountMessages() + Constantes.CANTIDAD_UNIDAD_INTEGER);
            completionsExistente.setLastSendMessage(completions.getRoleUser());
            completionsExistente.setLastResponseMessage(completions.getRoleContent());
            completionsExistente.setUpdDate(fechaActualTime.toLocalDate());
            completionsExistente.setUpdDatetime(fechaActualTime);
            completionsExistente.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

            //Guardar la cabecera actualizada
            cabConsultaIADAO.modificar(completionsExistente);
        }

        DetailConsultaIA detailConsultaIA = new DetailConsultaIA();

        detailConsultaIA.setUserId(completions.getUserId());
        detailConsultaIA.setModel(completions.getModel());
        detailConsultaIA.setRoleSystem(completions.getRoleSystem());
        detailConsultaIA.setSendMessage(completions.getRoleUser());
        detailConsultaIA.setTemperature(completions.getTemperature());
        detailConsultaIA.setFechaSend(completions.getFechaSend());
        detailConsultaIA.setFechaResponse(completions.getFechaResponse());
        detailConsultaIA.setIdGpt(completions.getIdGpt());
        detailConsultaIA.setObject(completions.getObject());
        detailConsultaIA.setCreated(completions.getCreated());
        detailConsultaIA.setModelResponse(completions.getModelResponse());
        detailConsultaIA.setRoleResponse(completions.getRoleResponse());
        detailConsultaIA.setResponseMessage(completions.getRoleContent());
        detailConsultaIA.setRefusal(completions.getRefusal());
        detailConsultaIA.setLogprobs(completions.getLogprobs());
        detailConsultaIA.setFinishReason(completions.getFinishReason());
        detailConsultaIA.setPromptTokens(completions.getPromptTokens());
        detailConsultaIA.setCompletionTokens(completions.getCompletionTokens());
        detailConsultaIA.setTotalTokens(completions.getTotalTokens());
        detailConsultaIA.setCachedTokens(completions.getCachedTokens());
        detailConsultaIA.setAudioTokens(completions.getAudioTokens());
        detailConsultaIA.setCompletionReasoningTokens(completions.getCompletionReasoningTokens());
        detailConsultaIA.setCompletionAudioTokens(completions.getCompletionAudioTokens());
        detailConsultaIA.setCompletionAceptedTokens(completions.getCompletionAceptedTokens());
        detailConsultaIA.setCompletionRejectedTokens(completions.getCompletionRejectedTokens());
        detailConsultaIA.setServiceTier(completions.getServiceTier());
        detailConsultaIA.setSystemFingerprint(completions.getSystemFingerprint());
        detailConsultaIA.setConfigurationsId(completions.getConfigurationsId());
        detailConsultaIA.setSessionUID(completions.getSessionUID());
        detailConsultaIA.setStatus(completions.getStatus());
        detailConsultaIA.setRegDate(fechaActualTime.toLocalDate());
        detailConsultaIA.setRegDatetime(fechaActualTime);
        detailConsultaIA.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        detailConsultaIADAO.registrar(detailConsultaIA);
    }
}
