package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.SIJCabGeminiChatDAO;
import pj.gob.pe.metricas.model.entities.SIJCabGeminiChat;
import pj.gob.pe.metricas.repository.GenericRepo;
import pj.gob.pe.metricas.repository.SIJCabGeminiChatRepo;

@Repository
@RequiredArgsConstructor
public class SIJCabGeminiChatDAOImpl extends GenericDAOImpl<SIJCabGeminiChat, Long> implements SIJCabGeminiChatDAO {

    private final SIJCabGeminiChatRepo repo;

    @Override
    protected GenericRepo<SIJCabGeminiChat, Long> getRepo() {
        return repo;
    }
}
