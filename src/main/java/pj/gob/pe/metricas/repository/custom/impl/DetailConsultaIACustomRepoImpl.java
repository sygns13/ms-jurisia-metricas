package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.model.entities.DetailConsultaIA;
import pj.gob.pe.metricas.repository.custom.DetailConsultaIACustomRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DetailConsultaIACustomRepoImpl implements DetailConsultaIACustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DetailConsultaIA> getDetailConsultaIA(Map<String, Object> filters, Map<String, Object> notEqualFilters, Map<String, Object> filtersFecha, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta principal para obtener los datos paginados
        CriteriaQuery<DetailConsultaIA> query = cb.createQuery(DetailConsultaIA.class);
        Root<DetailConsultaIA> cabConsultaIA = query.from(DetailConsultaIA.class);

        // Construir predicados y subconsulta (similar al código anterior)
        Predicate mainPredicate = buildPredicate(cabConsultaIA, cb, filters, notEqualFilters, filtersFecha);

        query.select(cabConsultaIA).where(mainPredicate);

        // Aplicar ordenamiento desde Pageable
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(cabConsultaIA.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(cabConsultaIA.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        }

        // Ejecutar consulta paginada
        List<DetailConsultaIA> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Consulta para obtener el total de elementos (count)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DetailConsultaIA> countRoot = countQuery.from(DetailConsultaIA.class);
        Predicate countPredicate = buildPredicate(countRoot, cb, filters, notEqualFilters, filtersFecha);

        // Contar el total de grupos únicos de "session" que cumplen los filtros
        countQuery.select(cb.count(countRoot)).where(countPredicate);

        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalElements);
    }

    // Método helper para construir predicados dinámicos
    private Predicate buildPredicate(
            Root<DetailConsultaIA> root,
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
            Root<DetailConsultaIA> root,
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
