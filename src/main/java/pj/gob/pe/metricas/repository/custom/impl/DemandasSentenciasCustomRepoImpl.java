package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.model.entities.DemandasSentenciasToKafka;
import pj.gob.pe.metricas.repository.custom.DemandasSentenciasCustomRepo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DemandasSentenciasCustomRepoImpl implements DemandasSentenciasCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DemandasSentenciasToKafka> getGeneralDemandasSentencias(Map<String, Object> filters, Map<String, Object> notEqualFilters, Map<String, Object> filtersFecha, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta principal para obtener los datos paginados
        CriteriaQuery<DemandasSentenciasToKafka> query = cb.createQuery(DemandasSentenciasToKafka.class);
        Root<DemandasSentenciasToKafka> demandasSentencias = query.from(DemandasSentenciasToKafka.class);

        // Construir predicados dinámicos
        Predicate mainPredicate = buildPredicate(demandasSentencias, cb, filters, notEqualFilters, filtersFecha);

        query.select(demandasSentencias).where(mainPredicate);

        // Aplicar ordenamiento desde Pageable
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(demandasSentencias.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(demandasSentencias.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        }

        // Ejecutar consulta paginada
        List<DemandasSentenciasToKafka> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Consulta para obtener el total de elementos (count)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DemandasSentenciasToKafka> countRoot = countQuery.from(DemandasSentenciasToKafka.class);
        Predicate countPredicate = buildPredicate(countRoot, cb, filters, notEqualFilters, filtersFecha);

        countQuery.select(cb.count(countRoot)).where(countPredicate);

        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalElements);
    }

    @Override
    public Long getTotalDemandasSentencias(
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta para obtener el total de elementos (count)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DemandasSentenciasToKafka> countRoot = countQuery.from(DemandasSentenciasToKafka.class);
        Predicate countPredicate = buildPredicate(countRoot, cb, filters, notEqualFilters);

        countQuery.select(cb.count(countRoot)).where(countPredicate);

        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        return totalElements;
    }

    // Método helper para construir predicados dinámicos (sin filtros de fecha)
    private Predicate buildPredicate(
            Root<DemandasSentenciasToKafka> root,
            CriteriaBuilder cb,
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters) {
        return buildPredicate(root, cb, filters, notEqualFilters, null);
    }

    // Método helper para construir predicados dinámicos
    private Predicate buildPredicate(
            Root<DemandasSentenciasToKafka> root,
            CriteriaBuilder cb,
            Map<String, Object> filters,
            Map<String, Object> notEqualFilters,
            Map<String, Object> filtersFecha) {

        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Procesar filtros "OR", "AND" y "IN"
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

        // Procesar filtros de fecha sobre fechaSend (LocalDateTime)
        if (filtersFecha != null) {
            if (filtersFecha.containsKey("fechaInicio") && filtersFecha.get("fechaInicio") != null) {
                LocalDate fechaInicio = (LocalDate) filtersFecha.get("fechaInicio");
                andPredicates.add(cb.greaterThanOrEqualTo(root.get("fechaSend"), fechaInicio.atStartOfDay()));
            }
            if (filtersFecha.containsKey("fechaFin") && filtersFecha.get("fechaFin") != null) {
                LocalDate fechaFin = (LocalDate) filtersFecha.get("fechaFin");
                andPredicates.add(cb.lessThanOrEqualTo(root.get("fechaSend"), fechaFin.atTime(LocalTime.MAX)));
            }
        }

        // Combinar todos los predicados
        Predicate orPredicate = orPredicates.isEmpty() ? cb.conjunction() : cb.or(orPredicates.toArray(new Predicate[0]));
        Predicate andPredicate = andPredicates.isEmpty() ? cb.conjunction() : cb.and(andPredicates.toArray(new Predicate[0]));
        Predicate notEqualPredicate = notEqualPredicates.isEmpty() ? cb.conjunction() : cb.and(notEqualPredicates.toArray(new Predicate[0]));

        return cb.and(orPredicate, andPredicate, notEqualPredicate);
    }
}
