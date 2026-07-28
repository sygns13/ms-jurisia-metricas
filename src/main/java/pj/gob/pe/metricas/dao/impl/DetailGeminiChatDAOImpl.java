package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DetailGeminiChatDAO;
import pj.gob.pe.metricas.model.entities.DetailGeminiChat;
import pj.gob.pe.metricas.repository.DetailGeminiChatRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class DetailGeminiChatDAOImpl extends GenericDAOImpl<DetailGeminiChat, Long> implements DetailGeminiChatDAO {

    private final DetailGeminiChatRepo repo;

    @Override
    protected GenericRepo<DetailGeminiChat, Long> getRepo() {
        return repo;
    }
}
