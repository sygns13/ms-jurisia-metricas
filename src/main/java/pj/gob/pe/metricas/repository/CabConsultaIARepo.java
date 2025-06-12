package pj.gob.pe.metricas.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.repository.custom.CabConsultaIACustomRepo;

public interface CabConsultaIARepo extends GenericRepo<CabConsultaIA, Long>, CabConsultaIACustomRepo {

    @Query(
            value = "select " +
                    "id, userId, countMessages, firstSendMessage, lastSendMessage, firstResponseMessage, lastResponseMessage, sessionUID, regDate, regDatetime, regTimestamp, updDate, updDatetime, updTimestamp " +
                    "from JURISDB_METRICS.CabConsultaIA " +
                    "where " +
                    "sessionUID = :sessionUID " +
                    "limit 1; ",
            nativeQuery = true
    )
    CabConsultaIA findBySessionUID(@Param("sessionUID") String sessionUID);
}
