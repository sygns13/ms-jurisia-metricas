package pj.gob.pe.metricas.repository;

import pj.gob.pe.metricas.model.entities.CabGeminiChat;

public interface CabGeminiChatRepo extends GenericRepo<CabGeminiChat, Long> {

    CabGeminiChat findFirstBySessionUID(String sessionUID);
}
