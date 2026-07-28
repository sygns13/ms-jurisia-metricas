package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.CabGeminiChatDAO;
import pj.gob.pe.metricas.model.entities.CabGeminiChat;
import pj.gob.pe.metricas.repository.CabGeminiChatRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class CabGeminiChatDAOImpl extends GenericDAOImpl<CabGeminiChat, Long> implements CabGeminiChatDAO {

    private final CabGeminiChatRepo repo;

    @Override
    protected GenericRepo<CabGeminiChat, Long> getRepo() {
        return repo;
    }

    @Override
    public CabGeminiChat findBySessionUID(String sessionUID) {
        return repo.findFirstBySessionUID(sessionUID);
    }
}
