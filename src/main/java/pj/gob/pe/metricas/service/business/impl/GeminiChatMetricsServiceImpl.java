package pj.gob.pe.metricas.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pj.gob.pe.metricas.dao.CabGeminiChatDAO;
import pj.gob.pe.metricas.dao.DetailGeminiChatDAO;
import pj.gob.pe.metricas.dao.DetailGeminiChatFilesDAO;
import pj.gob.pe.metricas.dao.SIJCabGeminiChatDAO;
import pj.gob.pe.metricas.dao.SIJDetailGeminiChatDAO;
import pj.gob.pe.metricas.model.beans.GeminiChatFileToKafka;
import pj.gob.pe.metricas.model.beans.GeminiChatToKafka;
import pj.gob.pe.metricas.model.entities.CabGeminiChat;
import pj.gob.pe.metricas.model.entities.DetailGeminiChat;
import pj.gob.pe.metricas.model.entities.DetailGeminiChatFiles;
import pj.gob.pe.metricas.model.entities.SIJCabGeminiChat;
import pj.gob.pe.metricas.model.entities.SIJDetailGeminiChat;
import pj.gob.pe.metricas.service.business.GeminiChatMetricsService;
import pj.gob.pe.metricas.utils.Constantes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

/**
 * Misma lógica general que {@link ConsultaIAServiceImpl#RegistrarConsultaIA} (chat ChatGPT):
 * si la conversación (sessionUID) no existe se registra la cabecera y sus sedes/instancias
 * (SIJCabGeminiChat); si existe, se actualizan contadores y últimos mensajes. Siempre se
 * registra el detalle del turno, sus adjuntos y el enlace SIJ del detalle.
 */
@Service
@RequiredArgsConstructor
public class GeminiChatMetricsServiceImpl implements GeminiChatMetricsService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatMetricsServiceImpl.class);

    private final CabGeminiChatDAO cabGeminiChatDAO;
    private final DetailGeminiChatDAO detailGeminiChatDAO;
    private final DetailGeminiChatFilesDAO detailGeminiChatFilesDAO;
    private final SIJCabGeminiChatDAO sIJCabGeminiChatDAO;
    private final SIJDetailGeminiChatDAO sIJDetailGeminiChatDAO;

    @Override
    public void RegistrarGeminiChat(GeminiChatToKafka geminiChat) throws Exception {

        LocalDateTime fechaActualTime = LocalDateTime.now();

        List<GeminiChatFileToKafka> files = geminiChat.getFiles() != null
                ? geminiChat.getFiles()
                : Collections.emptyList();
        Integer hasFiles = files.isEmpty() ? 0 : 1;

        // ============================================================
        // Cabecera de la conversación (insert la primera vez, update después)
        // ============================================================
        CabGeminiChat cabExistente = cabGeminiChatDAO.findBySessionUID(geminiChat.getSessionUID());

        if (cabExistente == null || cabExistente.getId() == null) {

            CabGeminiChat cabGeminiChat = new CabGeminiChat();

            cabGeminiChat.setUserId(geminiChat.getUserId());
            cabGeminiChat.setModel(geminiChat.getModel());
            cabGeminiChat.setCountMessages(Constantes.CANTIDAD_UNIDAD_INTEGER);
            cabGeminiChat.setFirstSendMessage(geminiChat.getPrompt());
            cabGeminiChat.setLastSendMessage(geminiChat.getPrompt());
            cabGeminiChat.setFirstResponseMessage(geminiChat.getResponse());
            cabGeminiChat.setLastResponseMessage(geminiChat.getResponse());
            cabGeminiChat.setSessionUID(geminiChat.getSessionUID());
            cabGeminiChat.setHasFiles(hasFiles);
            cabGeminiChat.setCountFiles(files.size());
            cabGeminiChat.setRegDate(fechaActualTime.toLocalDate());
            cabGeminiChat.setRegDatetime(fechaActualTime);
            cabGeminiChat.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

            cabGeminiChatDAO.registrar(cabGeminiChat);

            // Sedes e instancias del usuario a nivel de conversación (best-effort)
            if (geminiChat.getSedes() != null) {
                geminiChat.getSedes().forEach(sede -> {
                    sede.getInstancias().forEach(instancia -> {
                        SIJCabGeminiChat sIJCabGeminiChat = new SIJCabGeminiChat();

                        sIJCabGeminiChat.setUserId(geminiChat.getUserId());
                        sIJCabGeminiChat.setCodSede(sede.getCodSede());
                        sIJCabGeminiChat.setCodInstancia(instancia.getCodInstancia());
                        sIJCabGeminiChat.setSede(sede.getSede());
                        sIJCabGeminiChat.setInstancia(instancia.getInstancia());
                        sIJCabGeminiChat.setSessionUID(geminiChat.getSessionUID());
                        sIJCabGeminiChat.setRegDate(fechaActualTime.toLocalDate());
                        sIJCabGeminiChat.setRegDatetime(fechaActualTime);
                        sIJCabGeminiChat.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

                        try {
                            sIJCabGeminiChatDAO.registrar(sIJCabGeminiChat);
                        } catch (Exception e) {
                            logger.debug(e.getMessage());
                        }
                    });
                });
            }

        } else {
            cabExistente.setCountMessages(cabExistente.getCountMessages() + Constantes.CANTIDAD_UNIDAD_INTEGER);
            cabExistente.setLastSendMessage(geminiChat.getPrompt());
            cabExistente.setLastResponseMessage(geminiChat.getResponse());
            if (hasFiles == 1) {
                cabExistente.setHasFiles(hasFiles);
            }
            Integer countFilesActual = cabExistente.getCountFiles() != null ? cabExistente.getCountFiles() : 0;
            cabExistente.setCountFiles(countFilesActual + files.size());
            cabExistente.setUpdDate(fechaActualTime.toLocalDate());
            cabExistente.setUpdDatetime(fechaActualTime);
            cabExistente.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

            cabGeminiChatDAO.modificar(cabExistente);
        }

        // ============================================================
        // Detalle del turno
        // ============================================================
        DetailGeminiChat detailGeminiChat = new DetailGeminiChat();

        detailGeminiChat.setUserId(geminiChat.getUserId());
        detailGeminiChat.setModel(geminiChat.getModel());
        detailGeminiChat.setRoleSystem(geminiChat.getRoleSystem());
        detailGeminiChat.setSendMessage(geminiChat.getPrompt());
        detailGeminiChat.setTemperature(geminiChat.getTemperature());
        detailGeminiChat.setFechaSend(geminiChat.getFechaSend());
        detailGeminiChat.setFechaResponse(geminiChat.getFechaResponse());
        detailGeminiChat.setResponseMessage(geminiChat.getResponse());
        detailGeminiChat.setTimeSeconds(geminiChat.getTimeSeconds());
        detailGeminiChat.setConfigurationsId(geminiChat.getConfigurationsId());
        detailGeminiChat.setSessionUID(geminiChat.getSessionUID());
        detailGeminiChat.setStatus(geminiChat.getStatus());
        detailGeminiChat.setHasFiles(hasFiles);
        detailGeminiChat.setRegDate(fechaActualTime.toLocalDate());
        detailGeminiChat.setRegDatetime(fechaActualTime);
        detailGeminiChat.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        DetailGeminiChat finalDetail = detailGeminiChatDAO.registrar(detailGeminiChat);

        // ============================================================
        // Adjuntos del turno (nombre original + URI gs://)
        // ============================================================
        files.forEach(file -> {
            DetailGeminiChatFiles detailFile = new DetailGeminiChatFiles();

            detailFile.setDetailGeminiChatId(finalDetail.getId());
            detailFile.setUserId(geminiChat.getUserId());
            detailFile.setSessionUID(geminiChat.getSessionUID());
            detailFile.setFileName(file.getFileName());
            detailFile.setMimeType(file.getMimeType());
            detailFile.setSizeBytes(file.getSizeBytes());
            detailFile.setGcsUri(file.getGcsUri());
            detailFile.setStatus(file.getStatus());
            detailFile.setRegDate(fechaActualTime.toLocalDate());
            detailFile.setRegDatetime(fechaActualTime);
            detailFile.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

            try {
                detailGeminiChatFilesDAO.registrar(detailFile);
            } catch (Exception e) {
                logger.error("Error registrando adjunto de métricas Gemini: {}", e.getMessage());
            }
        });

        // ============================================================
        // Sedes e instancias del usuario a nivel de detalle (best-effort)
        // ============================================================
        if (geminiChat.getSedes() != null) {
            geminiChat.getSedes().forEach(sede -> {
                sede.getInstancias().forEach(instancia -> {
                    SIJDetailGeminiChat sIJDetailGeminiChat = new SIJDetailGeminiChat();

                    sIJDetailGeminiChat.setUserId(geminiChat.getUserId());
                    sIJDetailGeminiChat.setDetailGeminiChatId(finalDetail.getId());
                    sIJDetailGeminiChat.setCodSede(sede.getCodSede());
                    sIJDetailGeminiChat.setCodInstancia(instancia.getCodInstancia());
                    sIJDetailGeminiChat.setSede(sede.getSede());
                    sIJDetailGeminiChat.setInstancia(instancia.getInstancia());
                    sIJDetailGeminiChat.setSessionUID(finalDetail.getSessionUID());
                    sIJDetailGeminiChat.setRegDate(fechaActualTime.toLocalDate());
                    sIJDetailGeminiChat.setRegDatetime(fechaActualTime);
                    sIJDetailGeminiChat.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

                    try {
                        sIJDetailGeminiChatDAO.registrar(sIJDetailGeminiChat);
                    } catch (Exception e) {
                        logger.debug(e.getMessage());
                    }
                });
            });
        }
    }
}
