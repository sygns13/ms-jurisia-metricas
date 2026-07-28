package pj.gob.pe.metricas.service.business;

import pj.gob.pe.metricas.model.beans.GeminiChatToKafka;

/**
 * Persistencia de las métricas del chat conversacional con Gemini (tópico
 * judicial-metrics-gemini-chats). Misma lógica general que {@link ConsultaIAService}
 * para el chat ChatGPT: cabecera por conversación + detalle por turno + enlace SIJ,
 * más el registro de los adjuntos de cada turno.
 */
public interface GeminiChatMetricsService {

    void RegistrarGeminiChat(GeminiChatToKafka geminiChat) throws Exception;
}
