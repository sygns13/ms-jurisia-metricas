package pj.gob.pe.metricas.dao;

import pj.gob.pe.metricas.model.entities.CabGeminiChat;

public interface CabGeminiChatDAO extends GenericDAO<CabGeminiChat, Long> {

    CabGeminiChat findBySessionUID(String sessionUID);
}
