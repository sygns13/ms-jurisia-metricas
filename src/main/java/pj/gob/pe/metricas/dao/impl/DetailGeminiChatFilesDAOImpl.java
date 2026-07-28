package pj.gob.pe.metricas.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.dao.DetailGeminiChatFilesDAO;
import pj.gob.pe.metricas.model.entities.DetailGeminiChatFiles;
import pj.gob.pe.metricas.repository.DetailGeminiChatFilesRepo;
import pj.gob.pe.metricas.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class DetailGeminiChatFilesDAOImpl extends GenericDAOImpl<DetailGeminiChatFiles, Long> implements DetailGeminiChatFilesDAO {

    private final DetailGeminiChatFilesRepo repo;

    @Override
    protected GenericRepo<DetailGeminiChatFiles, Long> getRepo() {
        return repo;
    }
}
