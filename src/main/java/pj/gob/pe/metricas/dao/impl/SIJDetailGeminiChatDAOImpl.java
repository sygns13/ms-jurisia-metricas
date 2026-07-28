package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.SIJDetailGeminiChatDAO;
import pj.gob.pe.metricas.model.entities.SIJDetailGeminiChat;
import pj.gob.pe.metricas.repository.GenericRepo;
import pj.gob.pe.metricas.repository.SIJDetailGeminiChatRepo;

@Repository
@RequiredArgsConstructor
public class SIJDetailGeminiChatDAOImpl extends GenericDAOImpl<SIJDetailGeminiChat, Long> implements SIJDetailGeminiChatDAO {

    private final SIJDetailGeminiChatRepo repo;

    @Override
    protected GenericRepo<SIJDetailGeminiChat, Long> getRepo() {
        return repo;
    }
}
