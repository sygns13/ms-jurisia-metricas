package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.model.entities.CabConsultaIA;
import pj.gob.pe.metricas.model.entities.SIJCabConsultaIA;
import pj.gob.pe.metricas.repository.custom.SIJCabConsultaIACustomRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SIJCabConsultaIACustomRepoImpl implements SIJCabConsultaIACustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SIJCabConsultaIA> getGeneralConsultaIA(Map<String, Object> filters, Map<String, Object> notEqualFilters, Map<String, Object> filtersFecha) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta principal para obtener los datos paginados
        CriteriaQuery<SIJCabConsultaIA> query = cb.createQuery(SIJCabConsultaIA.class);
        Root<SIJCabConsultaIA> sIJCabConsultaIA = query.from(SIJCabConsultaIA.class);

        // Hacer JOIN con Dependencia
        Join<SIJCabConsultaIA, CabConsultaIA> cabConsultaIAJoin = sIJCabConsultaIA.join("cabConsultaIA");

        // Construir predicados y subconsulta (similar al código anterior)
        Predicate mainPredicate = buildPredicate(sIJCabConsultaIA, cb, filters, notEqualFilters, filtersFecha);

        query.select(sIJCabConsultaIA).where(mainPredicate);

        // Ejecutar consulta paginada
        List<SIJCabConsultaIA> resultList = entityManager.createQuery(query).getResultList();

        return resultList;
    }

    // Método helper para construir predicados dinámicos
    private Predicate buildPredicate(
            Root<SIJCabConsultaIA> root,
            CriteriaBuilder cb,
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters) {

        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Procesar filtros "OR" y "AND"
        filters.forEach((key, value) -> {
            if (value != null) {
                if (key.startsWith("or_")) {
                    orPredicates.add(cb.equal(root.get(key.replace("or_", "")), value));
                } else {
                    andPredicates.add(cb.equal(root.get(key), value));
                }
            }
        });

        // Procesar filtros "NOT EQUAL"
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(root.get(key), value));
            }
        });

        // Combinar todos los predicados
        Predicate orPredicate = orPredicates.isEmpty() ? cb.conjunction() : cb.or(orPredicates.toArray(new Predicate[0]));
        Predicate andPredicate = andPredicates.isEmpty() ? cb.conjunction() : cb.and(andPredicates.toArray(new Predicate[0]));
        Predicate notEqualPredicate = notEqualPredicates.isEmpty() ? cb.conjunction() : cb.and(notEqualPredicates.toArray(new Predicate[0]));

        return cb.and(orPredicate, andPredicate, notEqualPredicate);
    }

    // Método helper para construir predicados dinámicos
    private Predicate buildPredicate(
            Root<SIJCabConsultaIA> root,
            CriteriaBuilder cb,
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha) {

        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Procesar filtros "OR" y "AND"
        filters.forEach((key, value) -> {
            if (value != null) {
                if (key.startsWith("or_")) {
                    orPredicates.add(cb.equal(root.get(key.replace("or_", "")), value));
                } else if (key.startsWith("list_")) {
                    Path<Object> path = root.get(key.replace("list_", ""));
                    CriteriaBuilder.In<Object> inClause = cb.in(path);

                    List<?> valores = (List<?>) value;
                    if (valores != null && !valores.isEmpty()) {
                        for (Object v : valores) {
                            inClause.value(v);
                        }
                        andPredicates.add(inClause);
                    }
                } else {
                    andPredicates.add(cb.equal(root.get(key), value));
                }
            }
        });

        // Procesar filtros "NOT EQUAL"
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(root.get(key), value));
            }
        });

        // Procesar filtros de fecha
        if (filtersFecha != null) {
            if (filtersFecha.containsKey("fechaInicio") && filtersFecha.get("fechaInicio") != null) {
                andPredicates.add(cb.greaterThanOrEqualTo(root.get("regDate"), (java.time.LocalDate) filtersFecha.get("fechaInicio")));
            }
            if (filtersFecha.containsKey("fechaFin") && filtersFecha.get("fechaFin") != null) {
                andPredicates.add(cb.lessThanOrEqualTo(root.get("regDate"), (java.time.LocalDate) filtersFecha.get("fechaFin")));
            }
        }

        // Combinar todos los predicados
        Predicate orPredicate = orPredicates.isEmpty() ? cb.conjunction() : cb.or(orPredicates.toArray(new Predicate[0]));
        Predicate andPredicate = andPredicates.isEmpty() ? cb.conjunction() : cb.and(andPredicates.toArray(new Predicate[0]));
        Predicate notEqualPredicate = notEqualPredicates.isEmpty() ? cb.conjunction() : cb.and(notEqualPredicates.toArray(new Predicate[0]));

        return cb.and(orPredicate, andPredicate, notEqualPredicate);
    }
}
